//直接於body後面新增一個id的div，然後append到裡面
function templateInitializer(templatePath, id, callback) {
    var newDiv = document.createElement('div');
    newDiv.id = id;
    document.body.appendChild(newDiv);
    //使用jquery將template嵌入新的div中
    $('#' + id).load(templatePath, callback);
}

function domTemplateInitializer(domTemplate, id, callback) {
    var newDiv = document.createElement('div');
    newDiv.id = id;
    document.body.appendChild(newDiv);
    $('#' + id).append($.parseHTML(domTemplate));
    return callback();
}