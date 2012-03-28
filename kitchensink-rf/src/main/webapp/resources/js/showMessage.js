function showMessage(message) {
    jQuery('<div />', {class: 'rf-p-hdr'}).prependTo('#newMemberMessages').text(message).fadeOut(3000, function() {
      $(this).remove();
    });
}