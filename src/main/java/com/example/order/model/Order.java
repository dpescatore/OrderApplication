package com.example.order.model;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class Order {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    private String email;
    @NotBlank
    private String cellNumber;
    @NotNull(message = "Please enter photo type")
    private PhotoType photoType;
    private String logisticInfo;
    private Date dateTime;
    private Date creationDate;
    private Date lastModifyDate;
    private Long photographerId;
    private String photoZip;
    private State state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getLogisticInfo() {
        return logisticInfo;
    }

    public void setLogisticInfo(String logisticInfo) {
        this.logisticInfo = logisticInfo;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public PhotoType getPhotoType() {
        return this.photoType;
    }

    public void setPhotoType(PhotoType photoType) {
        this.photoType = photoType;
    }

    public Long getPhotographerId() {
        return this.photographerId;
    }

    public void setPhotographerId(Long photographerId) {
        this.photographerId = photographerId;
    }

    public String getPhotoZip() {
        return this.photoZip;
    }

    public void setPhotoZip(String photoZip) {
        this.photoZip = photoZip;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", surname='" + getSurname() + "'" +
            ", email='" + getEmail() + "'" +
            ", cellNumber='" + getCellNumber() + "'" +
            ", photoType='" + getPhotoType() + "'" +
            ", logisticInfo='" + getLogisticInfo() + "'" +
            ", dateTime='" + getDateTime() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", lastModifyDate='" + getLastModifyDate() + "'" +
            ", photographerId='" + getPhotographerId() + "'" +
            ", photoZip='" + getPhotoZip() + "'" +
            ", state='" + getState() + "'" +
            "}";
    }
  

    public static enum State {
        UNSCHEDULED {
            @Override
            public State nextState() {
                return PENDING;
            }

            @Override
            public State cancel() {
                return CANCELED;
            }
        },
        
        PENDING {
            @Override
            public State nextState() {
                return ASSIGNED;
            }

            @Override
            public State previousState() {
                return UNSCHEDULED;
            }

            @Override
            public State cancel() {
                return CANCELED;
            }
        },
        ASSIGNED

        {
            @Override
            public State nextState() {
                return UPLOADED;
            }

            @Override
            public State previousState() {
                return PENDING;
            }

            @Override
            public State cancel() {
                return CANCELED;
            }
        }

        ,
        UPLOADED {
            @Override
            public State nextState() {
                return COMPLETED;
            }

            @Override
            public State previousState() {
                return ASSIGNED;
            }

            @Override
            public State cancel() {
                return CANCELED;
            }
        },
        COMPLETED, CANCELED;

        public State nextState() {
            return null;
        }

        public State previousState() {
            return this;
        }

        public State cancel() {
            return null;
        }
    }

    public static enum PhotoType {
        RealEstate, Food, Events
    }
}
