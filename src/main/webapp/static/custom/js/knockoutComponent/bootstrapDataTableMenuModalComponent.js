function createDiv(id) {
    const modal = document.createElement('div');
    modal.setAttribute("id", id);
}

function createBootstrapDataTableDom(param) {
    const table = document.createElement("table");
    table.id = param.tableId;
    table.setAttribute("content", "optionList");
    table.setAttribute("border", "0");
    table.setAttribute("width", "98%");
    table.setAttribute("cellspacing", "0");
    table.setAttribute("cellpadding", "0");
    const thead = document.createElement("thead");
    const tr = document.createElement("tr");
    if (param.th instanceof Array) {
        param.th.forEach(name => {
            const th = document.createElement("th");
            th.innerHTML = name;
            tr.appendChild(th);
        });
    }
    thead.appendChild(tr);
    table.appendChild(thead);
    return table;
}

function createBootstrapDataTableMenuModalDom(param) {
    const modal = document.createElement('div');
    modal.setAttribute("class", "modal fade");
    modal.setAttribute("id", param.modalId);
    modal.setAttribute("tabindex", "-1");
    modal.setAttribute("role", "dialog");
    modal.setAttribute("aria-labelledby", param.modalId + "Label");
    modal.setAttribute("aria-hidden", "true");
    modal.setAttribute("data-backdrop", "false");

    const dialog = document.createElement("div");
    dialog.setAttribute("class", "modal-dialog");
    if (param.size) {
        if (param.size == 'sm') {
            dialog.setAttribute("class", "modal-dialog modal-sm");
        }
        if (param.size == 'xl') {
            dialog.setAttribute("class", "modal-dialog modal-xl");
        }
    }
    dialog.setAttribute("role", "document");

    const title = document.createElement("h5");
    title.setAttribute("class", "modal-title");
    title.setAttribute("id", param.modalId + "Label");
    title.innerHTML = param.title;

    const closeButton = document.createElement("button");
    closeButton.setAttribute("type", "button");
    closeButton.setAttribute("class", "close");
    closeButton.setAttribute("data-dismiss", "modal");
    closeButton.setAttribute("aria-label", "Close");

    const closeSpan = document.createElement("span");
    closeSpan.setAttribute("aria-hidden", "true");
    closeSpan.innerHTML = "&times;";
    closeButton.appendChild(closeSpan);

    const content = document.createElement("div");
    content.setAttribute("class", "modal-content");

    const header = document.createElement("div");
    header.setAttribute("class", "modal-header");

    const body = document.createElement("div");
    body.setAttribute("class", "modal-body");

    const table = document.createElement("table");
    table.id = param.tableId;
    table.setAttribute("content", "optionList");
    table.setAttribute("border", "0");
    table.setAttribute("width", "98%");
    table.setAttribute("cellspacing", "0");
    table.setAttribute("cellpadding", "0");

    const thead = document.createElement("thead");
    const tr = document.createElement("tr");
    if (param.th instanceof Array) {
        param.th.forEach(name => {
            const th = document.createElement("th");
            th.innerHTML = name;
            tr.appendChild(th);
        });
    }
    thead.appendChild(tr);
    table.appendChild(thead);
    body.appendChild(table);
    const footer = document.createElement("div");
    footer.setAttribute("class", "modal-footer");

    const footerCloseButton = document.createElement("button");
    footerCloseButton.setAttribute("class", "btn btn-secondary");
    footerCloseButton.setAttribute("data-dismiss", "modal");
    footerCloseButton.innerHTML = "關閉";

    footer.appendChild(footerCloseButton);

    header.appendChild(title);
    header.appendChild(closeButton);
    content.appendChild(header);
    content.appendChild(body);
    content.appendChild(footer);

    dialog.appendChild(content);

    modal.appendChild(dialog);
    return modal;
}