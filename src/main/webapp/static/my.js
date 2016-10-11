function replyToComment(id, text) {
    $('#comment-parent-id').attr('value', id);
    $('#comment-parent-text').text(text);
    $('#comment-parent-clear').show();
}

function clearParentComment() {
    $('#comment-parent-id').attr('value', '');
    $('#comment-parent-text').text('');
    $('#comment-parent-clear').hide();
}