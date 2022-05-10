/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.svs.farm_app.entities;

/**
 * class to interact with DatabaseHandler (Farm assessment form type tables).
 * @author Benson
 */
public class FarmAssFormTypes {

    private String formTypeId;
    private String formId;
    private String formType;

    public String getFormTypeID() {
        return formTypeId;
    }

    public String getFormID() {
        return formId;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormTypeID(String formTypeId) {
        this.formTypeId = formTypeId;
    }

    public void setFormID(String formId) {
        this.formId = formId;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

}
