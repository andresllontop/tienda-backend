package com.codedoblea.tienda.utilities;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BeanCrud<T>{

    private String messageServer;
    private BeanPagination beanPagination;
    private T classGeneric;

    public String getMessageServer() {
        return messageServer;
    }

    public void setMessageServer(String messageServer) {
        this.messageServer = messageServer;
    }

    public BeanPagination getBeanPagination() {
        return beanPagination;
    }

    public void setBeanPagination(BeanPagination beanPagination) {
        this.beanPagination = beanPagination;
    }

    public T getClassGeneric() {
        return classGeneric;
    }

    public void setClassGeneric(T classGeneric) {
        this.classGeneric = classGeneric;
    }

    @Override
    public String toString() {
        return "BeanCrud [messageServer=" + messageServer + 
                ", beanPagination=" + beanPagination +
                ", classGeneric=" + classGeneric +"]";
    }

}
