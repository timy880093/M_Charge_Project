package com.gateweb.orm.charge.entity.view;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "charge_package", schema = "public", indexes = {
        @Index(name = "package_pkey", unique = true, columnList = "package_id ASC")
})
public class ChargePackageView {
    private Long packageId;
    private String name;
    private String status;
    private SimpleUserView creator;
    private Timestamp createDate;
    private SimpleUserView modifier;
    private Timestamp modifyDate;

    public ChargePackageView() {
    }

    public ChargePackageView(ChargePackageView value) {
        this.packageId = value.packageId;
        this.name = value.name;
        this.status = value.status;
        this.creator = value.creator;
        this.createDate = value.createDate;
        this.modifier = value.modifier;
        this.modifyDate = value.modifyDate;
    }

    public ChargePackageView(Long packageId, String name, String status, SimpleUserView creator, Timestamp createDate, SimpleUserView modifier, Timestamp modifyDate) {
        this.packageId = packageId;
        this.name = name;
        this.status = status;
        this.creator = creator;
        this.modifier = modifier;
        this.createDate = createDate;
        this.modifyDate = modifyDate;
    }

    @Id
    @SequenceGenerator(name = "charge_package_package_id_seq",
            sequenceName = "charge_package_package_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "charge_package_package_id_seq")
    @Column(name = "package_id", unique = true, precision = 64)
    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @OneToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "user_id")
    public SimpleUserView getCreator() {
        return creator;
    }

    public void setCreator(SimpleUserView creator) {
        this.creator = creator;
    }

    @CreationTimestamp
    @Column(name = "create_date")
    public Timestamp getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @OneToOne
    @JoinColumn(name = "modifier_id", referencedColumnName = "user_id")
    public SimpleUserView getModifier() {
        return modifier;
    }

    public void setModifier(SimpleUserView modifier) {
        this.modifier = modifier;
    }

    @UpdateTimestamp
    @Column(name = "modify_date")
    public Timestamp getModifyDate() {
        return this.modifyDate;
    }

    public void setModifyDate(Timestamp modifyDate) {
        this.modifyDate = modifyDate;
    }

    @Override
    public String toString() {
        return "ChargePackageView{" +
                "packageId=" + packageId +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", creator=" + creator +
                ", createDate=" + createDate +
                ", modifier=" + modifier +
                ", modifyDate=" + modifyDate +
                '}';
    }

}
