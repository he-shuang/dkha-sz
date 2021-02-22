package com.dkha.model.aidoor.request;

import java.io.Serializable;
import java.util.List;

public class PersonAddList implements Serializable {

    private boolean registerComplete;

    private int threadCount;

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public boolean isRegisterComplete() {
        return registerComplete;
    }

    public void setRegisterComplete(boolean registerComplete) {
        this.registerComplete = registerComplete;
    }

    private List<PersonAdd> personAddList;

    public List<PersonAdd> getPersonAddList() {
        return personAddList;
    }

    public void setPersonAddList(List<PersonAdd> personAddList) {
        this.personAddList = personAddList;
    }
}
