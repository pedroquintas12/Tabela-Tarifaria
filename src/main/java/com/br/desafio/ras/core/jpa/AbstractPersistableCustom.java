package com.br.desafio.ras.core.jpa;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;

import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ClassUtils;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@SuperBuilder
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "deleted=0")
public abstract class   AbstractPersistableCustom<ID extends Serializable> implements Persistable<ID> {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;

    @JsonIgnore
    @Column(nullable = false, columnDefinition = "BIGINT DEFAULT 0")
    @Builder.Default
    private Long deleted = 0L;

    @JsonIgnore
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false, insertable = false,columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private Date createdDate;

    @JsonIgnore
    @LastModifiedDate
    @Column(name = "modified_date", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd HH:mm:ss")
    private Date modifiedDate;

    public ID getId() {
        return id;
    }

    protected void setId(final ID id) {
        this.id = id;
    }

    public Long isDeleted() {
        return deleted;
    }

    public void setDeleted(Long deleted) {
        this.deleted = deleted;
    }



    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Transient
    @JsonIgnore
    public boolean isNew() {
        return null == getId();
    }

    @Override
    public String toString() {
        return String.format("Entidade do tipo %s com id: %s", this.getClass().getName(), getId());
    }

    @Override
    public boolean equals(Object obj) {

        if (null == obj) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(ClassUtils.getUserClass(obj))) {
            return false;
        }

        AbstractPersistableCustom<?> that = (AbstractPersistableCustom<?>) obj;

        return null != this.getId() && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {

        int hashCode = 17;

        hashCode += null == getId() ? 0 : getId().hashCode() * 31;

        return hashCode;
    }
}

