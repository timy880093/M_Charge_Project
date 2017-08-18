package com.gate.web.messages;

/**
 * Created by simon on 2014/7/8.
 */
public class ErrorMessages {

    public String message ="";
    public String currentTimes = "";
    public String previousPage="";
    public String printStackMessage="";


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrentTimes() {
        return currentTimes;
    }

    public void setCurrentTimes(String currentTimes) {
        this.currentTimes = currentTimes;
    }

    public String getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
    }

    public String getPrintStackMessage() {
        return printStackMessage;
    }

    public void setPrintStackMessage(String printStackMessage) {
        this.printStackMessage = printStackMessage;
    }
}
