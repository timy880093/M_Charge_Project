function base64ToArrayBuffer(base64) {
    const binaryString = window.atob(base64); // Comment this if not using base64
    const bytes = new Uint8Array(binaryString.length);
    return bytes.map((byte, i) => binaryString.charCodeAt(i));
}

/**
 * 謹以此註解記念這些先烈們
 * https://medium.com/@riccardopolacci/download-file-in-javascript-from-bytea-6a0c5bb3bbdb
 * https://stackoverflow.com/questions/29393601/downloading-file-from-ajax-result-using-blob
 * https://stackoverflow.com/questions/27120757/failed-to-execute-createobjecturl-on-url
 * @param body
 * @param fileName
 */
function downloadFileFromBlob(body, fileName) {

    const blob = new Blob([body]);
    if (navigator.msSaveBlob) {
        // IE 10+
        navigator.msSaveBlob(blob, fileName);
    } else {
        const link = document.createElement('a');
        // Browsers that support HTML5 download attribute
        if (link.download !== undefined) {
            const url = URL.createObjectURL(blob);
            link.setAttribute('href', url);
            link.setAttribute('download', fileName);
            link.style.visibility = 'hidden';
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        }
    }
}