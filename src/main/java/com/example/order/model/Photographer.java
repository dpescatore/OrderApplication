package com.example.order.model;

import java.util.Objects;

public class Photographer {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String cellNumber;


    public Photographer() {
    }

    public Photographer(Long id, String name, String surname, String email, String cellNumber) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.cellNumber = cellNumber;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellNumber() {
        return this.cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public Photographer id(Long id) {
        setId(id);
        return this;
    }

    public Photographer name(String name) {
        setName(name);
        return this;
    }

    public Photographer surname(String surname) {
        setSurname(surname);
        return this;
    }

    public Photographer email(String email) {
        setEmail(email);
        return this;
    }

    public Photographer cellNumber(String cellNumber) {
        setCellNumber(cellNumber);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Photographer)) {
            return false;
        }
        Photographer photographer = (Photographer) o;
        return Objects.equals(id, photographer.id) && Objects.equals(name, photographer.name) && Objects.equals(surname, photographer.surname) && Objects.equals(email, photographer.email) && Objects.equals(cellNumber, photographer.cellNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, email, cellNumber);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", email='" + getEmail() + "'" +
            ", cellNumber='" + getCellNumber() + "'" +
            "}";
    }

}
