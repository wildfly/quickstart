
$undertow
    .alias("db", "jndi:java:jboss/datasources/KitchensinkMustacheQuickstartDS")
    .onGet("/index.html", {template: "index.html", headers: {'Content-Type': "text/html; charset=UTF-8"}, transactional: true}, ['db', function($exchange, db) {
        var ret = {};
        ret['members'] = db.select('select * from member order by name');
        return ret;
    }])
    .onPost("/index.html", {template: "index.html", headers: {'Content-Type': "text/html; charset=UTF-8"}, transactional: true}, ['db', '$entity:form', function($exchange, db, form) {

        var ret = {};
        var errorMessage = "";

        try {
            var ok = true;
            var name = form['name'];
            var email = form['email'];
            var phoneNumber = form['phoneNumber'];
            if (!name) {
                errorMessage += "Name is required\n";
                ok = false;
            }
            if(!email) {
                errorMessage += "Email is required\n";
                ok = false;
            }
            if(db.selectOne("select id from member where email=?", email) != null) {
                errorMessage += "Email is already registered\n";
                ok = false;
            }
            if(!phoneNumber) {
                errorMessage += "Phone is required\n";
                ok = false;
            }
            if(ok) {
                db.query("insert into member (name, email, phone_number) values (?,?,?)", name, email, phoneNumber);
                ret["infoMessage"] = name + " Registered!";
            } else {
                return {
                    newMemberName: form['name'],
                    newMemberEmail: form['email'],
                    newMemberPhoneNumber: form['phoneNumber'],
                    members:  db.select('select * from member order by name'),
                    errorMessage: errorMessage
                };
            }

        } catch (e) {
            errorMessage += ("Error========>" + e);
            ret["infoMessage"] = "";
        }
        ret['errorMessage'] = errorMessage;
        ret['members'] = db.select('select * from member order by name');
        return ret;
    }]);

//set up the DB
var db = $undertow.resolve("db");
try {
    db.query("create table member (id serial primary key not null, name varchar(100), email varchar(100) unique, phone_number varchar(100))");
    db.query("insert into member (id, name, email, phone_number) values (0, 'John Smith', 'john.smith@mailinator.jsp.com', '2125551212')");
    db.query("insert into member (id, name, email, phone_number) values (0, 'John Smith2', 'john.smith2@mailinator.jsp.com', '2125551212')");
} catch(e) {
    print("DB create failed")
}
