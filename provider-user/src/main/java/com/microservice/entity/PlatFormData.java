package com.microservice.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by cmonkey on 3/20/17.
 */
@Entity(name = "plat_form_data")
public class PlatFormData implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column
    private Date createTime;
    @Column
    private int regCount;
    @Column
    private int bankCount;
    @Column
    private int investCountOrDay;
    @Column
    private int investCount;
    @Column
    private int investNewCount;
    @Column
    private BigDecimal investSum;
    @Column
    private BigDecimal annualSum;
    @Column
    private BigDecimal investConvert;
    @Column
    private BigDecimal rechargeSum;
    @Column
    private BigDecimal cashWithSum;
    @Column
    private BigDecimal repaymentSum;
    @Column
    private BigDecimal interestSum;
    @Column
    private BigDecimal collectSum;
    @Column
    private BigDecimal redSum;
    @Column
    private BigDecimal cashSum;
    @Column
    private int originInvestCount;
    @Column
    private BigDecimal originInvestSum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getRegCount() {
        return regCount;
    }

    public void setRegCount(int regCount) {
        this.regCount = regCount;
    }

    public int getBankCount() {
        return bankCount;
    }

    public void setBankCount(int bankCount) {
        this.bankCount = bankCount;
    }

    public int getInvestCountOrDay() {
        return investCountOrDay;
    }

    public void setInvestCountOrDay(int investCountOrDay) {
        this.investCountOrDay = investCountOrDay;
    }

    public int getInvestCount() {
        return investCount;
    }

    public void setInvestCount(int investCount) {
        this.investCount = investCount;
    }

    public int getInvestNewCount() {
        return investNewCount;
    }

    public void setInvestNewCount(int investNewCount) {
        this.investNewCount = investNewCount;
    }

    public BigDecimal getInvestSum() {
        return investSum;
    }

    public void setInvestSum(BigDecimal investSum) {
        this.investSum = investSum;
    }

    public BigDecimal getAnnualSum() {
        return annualSum;
    }

    public void setAnnualSum(BigDecimal annualSum) {
        this.annualSum = annualSum;
    }

    public BigDecimal getInvestConvert() {
        return investConvert;
    }

    public void setInvestConvert(BigDecimal investConvert) {
        this.investConvert = investConvert;
    }

    public BigDecimal getRechargeSum() {
        return rechargeSum;
    }

    public void setRechargeSum(BigDecimal rechargeSum) {
        this.rechargeSum = rechargeSum;
    }

    public BigDecimal getCashWithSum() {
        return cashWithSum;
    }

    public void setCashWithSum(BigDecimal cashWithSum) {
        this.cashWithSum = cashWithSum;
    }

    public BigDecimal getRepaymentSum() {
        return repaymentSum;
    }

    public void setRepaymentSum(BigDecimal repaymentSum) {
        this.repaymentSum = repaymentSum;
    }

    public BigDecimal getInterestSum() {
        return interestSum;
    }

    public void setInterestSum(BigDecimal interestSum) {
        this.interestSum = interestSum;
    }

    public BigDecimal getCollectSum() {
        return collectSum;
    }

    public void setCollectSum(BigDecimal collectSum) {
        this.collectSum = collectSum;
    }

    public BigDecimal getRedSum() {
        return redSum;
    }

    public void setRedSum(BigDecimal redSum) {
        this.redSum = redSum;
    }

    public BigDecimal getCashSum() {
        return cashSum;
    }

    public void setCashSum(BigDecimal cashSum) {
        this.cashSum = cashSum;
    }

    public int getOriginInvestCount() {
        return originInvestCount;
    }

    public void setOriginInvestCount(int originInvestCount) {
        this.originInvestCount = originInvestCount;
    }

    public BigDecimal getOriginInvestSum() {
        return originInvestSum;
    }

    public void setOriginInvestSum(BigDecimal originInvestSum) {
        this.originInvestSum = originInvestSum;
    }
}
