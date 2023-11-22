/*
 * JBoss, Home of Professional Open Source.
 *
 * Copyright 2023 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

(() => {
    "use strict";

    window.addEventListener("load", () => {
        loadLogFiles();
        // Add a listener all the log buttons
        const logButtons = document.querySelector("#logButtons");
        if (logButtons.hasChildNodes()) {
            const buttons = logButtons.childNodes;
            for (const logButton of buttons) {
                logButton.addEventListener("click", (event) => {
                    const response = postLogs(event.target.id);
                    response.then((response) => {
                        if (response.status === 200) {
                            // Find the active tab and reload the data
                            const activeTab = document.querySelector(".nav-link.active");
                            const tbody = document.querySelector(".tab-pane.active tbody");
                            if (activeTab && tbody) {
                                loadLog(activeTab.textContent, tbody).then(response => {
                                    if (response && response.status !== 200) {
                                        response.text().then(t => {
                                            error(`Failed to log message. Reason: ${t}`);
                                        });
                                    }
                                });
                            } else {
                                // The log files have not been created, let's check again to see if they have
                                loadLogFiles();
                            }
                            const level = event.target.id ? event.target.id.toUpperCase() : "UNKNOWN"
                            success(`Logged a ${level} message`);
                        } else {
                            response.text().then(t => {
                                error(`Failed to log message. Reason: ${t}`);
                            });
                        }
                    });
                });
            }
        }
    });

    /**
     * Loads the log files
     */
    function loadLogFiles() {
        fetch("api/logs")
            .then((r) => {
                r.json().then((json) => {
                    const tabContainer = document.querySelector("#logTabs");
                    const logFiles = document.querySelector("#logFiles");
                    let index = 0;
                    // Create a new entry for each contact
                    for (let x in json) {
                        let selected;
                        if (index === 0) {
                            selected = "true";
                        } else {
                            selected = "false";
                        }
                        const logFile = json[x];
                        // id's cannot have dots, replace it with a dash
                        const id = `${logFile}`.replace(/\./g,'-');
                        // Create the tab
                        const tab = document.createElement("li");
                        tab.classList.add("nav-item");
                        tab.role = "presentation";
                        tabContainer.appendChild(tab);

                        // Create the button for the tab
                        const tabButton = document.createElement("button");
                        tab.appendChild(tabButton);
                        tabButton.classList.add("nav-link");
                        if (index === 0) {
                            tabButton.classList.add("active");
                        }
                        tabButton.id = id;
                        tabButton.setAttribute("data-bs-toggle", "tab");
                        tabButton.setAttribute("data-bs-target", `#${id}-pane`);
                        tabButton.type = "button";
                        tabButton.role = "tab";
                        tabButton.setAttribute("aria-controls", `${id}-pane`);
                        tabButton.textContent = logFile;
                        tabButton.setAttribute("aria-selected", selected);

                        // Create the table for the tab of the log entries
                        const logData = document.createElement("table");
                        logData.classList.add("table", "table-striped");
                        const tbody = document.createElement("tbody");
                        logData.appendChild(tbody);
                        tabButton.addEventListener("click", (event) => {
                            event.preventDefault();
                            loadLog(logFile, tbody);
                        });
                        // Create the data wrapper
                        const tabData = document.createElement("div");
                        if (index === 0) {
                            tabData.classList.add("tab-pane", "fade", "show", "active");
                            loadLog(logFile, tbody);
                        } else {
                            tabData.classList.add("tab-pane", "fade");
                        }
                        tabData.role = "tabpanel";
                        tabData.setAttribute("aria-labelledby", `${id}`);
                        tabData.setAttribute("tabindex", index);
                        tabData.id = `${id}-pane`;
                        tabData.appendChild(logData);
                        logFiles.appendChild(tabData);
                        index++;
                    }
                });
            });
    }

    async function loadLog(file, tbody) {
        // Clear the table
        while (tbody.firstChild) {
            tbody.removeChild(tbody.firstChild);
        }
        // Get the log file contents
        return await fetch("api/logs/read/" + file)
            .then(r => {
                r.json().then((json) => {
                    for (let x in json) {
                        const row = tbody.insertRow();
                        const rowData = row.insertCell();
                        const pre = document.createElement("pre");
                        const code= document.createElement("code");
                        pre.appendChild(code);
                        rowData.appendChild(pre);
                        code.textContent = json[x];
                        tbody.appendChild(row);
                    }
                });
            });
    }

    async function postLogs(level) {
        return await fetch(`api/logs/${level}`, {
            method: "POST"
        });
    }

    function success(message) {
        showAlert(message);
    }

    function error(message) {
        showAlert(message, "danger", "false");
    }

    function showAlert(message, type = "success", autoHide = "true") {
        const alertPlaceholder = document.querySelector("#liveAlertPlaceholder");
        const alert = document.querySelector("#alert").content.cloneNode(true).querySelector("div.toast");
        alert.setAttribute("data-bs-autohide", autoHide);
        alert.classList.add("text-bg-" + type);
        const body = alert.querySelector(".toast-body");
        body.textContent = message;
        alert.addEventListener("hidden.bs.toast", () => {
            alertPlaceholder.removeChild(alert);
        });
        const toast = new bootstrap.Toast(alert);
        toast.show();
        alertPlaceholder.append(alert);
    }
})()