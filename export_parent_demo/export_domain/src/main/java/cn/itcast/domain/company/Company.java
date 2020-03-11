package cn.itcast.domain.company;

import java.io.Serializable;
import java.util.Date;

public class Company implements Serializable{

	private String id               ; //主键ID
	private String name             ; //企业名称
	private Date expirationDate     ;  //租用的到期时间
	private String address          ; //企业地址
	private String licenseId       ;  //营业执照编号
	private String representative   ; //企业法人
	private String phone            ; //联系电话
	private String companySize     ; //企业规模
	private String industry         ; //企业所属行业
	private String remarks          ; //备注
	private Integer state           ; //状态 0 : 不可用 ,1:可用
	private Double balance          ; //余额
	private String city             ; //所属城市

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(String licenseId) {
		this.licenseId = licenseId;
	}

	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCompanySize() {
		return companySize;
	}

	public void setCompanySize(String companySize) {
		this.companySize = companySize;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "Company{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				", expirationDate=" + expirationDate +
				", address='" + address + '\'' +
				", licenseId='" + licenseId + '\'' +
				", representative='" + representative + '\'' +
				", phone='" + phone + '\'' +
				", companySize='" + companySize + '\'' +
				", industry='" + industry + '\'' +
				", remarks='" + remarks + '\'' +
				", state=" + state +
				", balance=" + balance +
				", city='" + city + '\'' +
				'}';
	}
}
