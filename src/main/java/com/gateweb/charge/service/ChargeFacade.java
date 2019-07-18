/*
 * $Header: $
 * This java source file is generated by pkliu on Tue Jan 30 14:38:15 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.charge.service;
import java.util.List;

import com.gateweb.charge.model.*;
import com.gateweb.charge.model.Company;

/**
 * @author pkliu
 *
 */
public interface ChargeFacade {

    /**
     * Create a new record in Database.
     */
	public PrinterEntity save(PrinterEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public PrinterEntity findPrinterById(
		java.lang.Integer printerId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<PrinterEntity> findPrinterAll() ;
    
    /**
     * Update a record in Database.
     */
    public PrinterEntity update(PrinterEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deletePrinter(
		java.lang.Integer printerId 

	) ;

    /**
     * search records from Database.
     */
    public List<PrinterEntity> searchBy(PrinterEntity bean) ;

    /**
     * search records from Database.
     */
    public List<PrinterEntity> searchLike(PrinterEntity bean) ;

	/**
     * Create a new record in Database.
     */
	public ChargeModeCycleEntity save(ChargeModeCycleEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public ChargeModeCycleEntity findChargeModeCycleById(
		java.lang.Integer chargeId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<ChargeModeCycleEntity> findChargeModeCycleAll() ;
    
    /**
     * Update a record in Database.
     */
    public ChargeModeCycleEntity update(ChargeModeCycleEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deleteChargeModeCycle(
		java.lang.Integer chargeId 

	) ;

    /**
     * search records from Database.
     */
    public List<ChargeModeCycleEntity> searchBy(ChargeModeCycleEntity bean) ;

    /**
     * search records from Database.
     */
    public List<ChargeModeCycleEntity> searchLike(ChargeModeCycleEntity bean) ;
    /**
     * Create a new record in Database.
     */
	public PackageModeEntity save(PackageModeEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public PackageModeEntity findPackageModeById(
		java.lang.Integer packageId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<PackageModeEntity> findPackageModeAll() ;
    
    /**
     * Update a record in Database.
     */
    public PackageModeEntity update(PackageModeEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deletePackageMode(
		java.lang.Integer packageId 

	) ;

    /**
     * search records from Database.
     */
    public List<PackageModeEntity> searchBy(PackageModeEntity bean) ;

    /**
     * search records from Database.
     */
    public List<PackageModeEntity> searchLike(PackageModeEntity bean) ;
    /**
     * Create a new record in Database.
     */
	public Company save(Company bean) ;

    /**
     * Retrieve a record from Database.
     */
	public Company findCompanyById(
		java.lang.Integer companyId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<Company> findCompanyAll() ;
    
    /**
     * Update a record in Database.
     */
    public Company update(Company bean) ;

    /**
     * Create a new record in Database.
     */
	public void deleteCompany(
		java.lang.Integer companyId 

	) ;
    /**
     * Create a new record in Database.
     */
	public CashDetailEntity save(CashDetailEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public CashDetailEntity findCashDetailById(
		java.lang.Integer cashDetailId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<CashDetailEntity> findCashDetailAll() ;
    
    /**
     * Update a record in Database.
     */
    public CashDetailEntity update(CashDetailEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deleteCashDetail(
		java.lang.Integer cashDetailId 

	) ;

    /**
     * search records from Database.
     */
    public List<CashDetailEntity> searchBy(CashDetailEntity bean) ;

    /**
     * search records from Database.
     */
    public List<CashDetailEntity> searchLike(CashDetailEntity bean) ;
    /**
     * Create a new record in Database.
     */
	public DealerCompanyEntity save(DealerCompanyEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public DealerCompanyEntity findDealerCompanyById(
		java.lang.Integer dealerCompanyId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<DealerCompanyEntity> findDealerCompanyAll() ;
    
    /**
     * Update a record in Database.
     */
    public DealerCompanyEntity update(DealerCompanyEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deleteDealerCompany(
		java.lang.Integer dealerCompanyId 

	) ;

    /**
     * search records from Database.
     */
    public List<DealerCompanyEntity> searchBy(DealerCompanyEntity bean) ;

    /**
     * search records from Database.
     */
    public List<DealerCompanyEntity> searchLike(DealerCompanyEntity bean) ;
    /**
     * Create a new record in Database.
     */
	public CommissionLogEntity save(CommissionLogEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public CommissionLogEntity findCommissionLogById(
		java.lang.Integer commissionLogId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<CommissionLogEntity> findCommissionLogAll() ;
    
    /**
     * Update a record in Database.
     */
    public CommissionLogEntity update(CommissionLogEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deleteCommissionLog(
		java.lang.Integer commissionLogId 

	) ;

    /**
     * search records from Database.
     */
    public List<CommissionLogEntity> searchBy(CommissionLogEntity bean) ;

    /**
     * search records from Database.
     */
    public List<CommissionLogEntity> searchLike(CommissionLogEntity bean) ;
    /**
     * Create a new record in Database.
     */
	public LogDataEntity save(LogDataEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public LogDataEntity findLogDataById(
		java.lang.Integer logId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<LogDataEntity> findLogDataAll() ;
    
    /**
     * Update a record in Database.
     */
    public LogDataEntity update(LogDataEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deleteLogData(
		java.lang.Integer logId 

	) ;

    /**
     * search records from Database.
     */
    public List<LogDataEntity> searchBy(LogDataEntity bean) ;

    /**
     * search records from Database.
     */
    public List<LogDataEntity> searchLike(LogDataEntity bean) ;
    /**
     * Create a new record in Database.
     */
	public InvoiceTypeEntity save(InvoiceTypeEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public InvoiceTypeEntity findInvoiceTypeById(
		java.lang.String typeCode 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<InvoiceTypeEntity> findInvoiceTypeAll() ;
    
    /**
     * Update a record in Database.
     */
    public InvoiceTypeEntity update(InvoiceTypeEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deleteInvoiceType(
		java.lang.String typeCode 

	) ;

    /**
     * search records from Database.
     */
    public List<InvoiceTypeEntity> searchBy(InvoiceTypeEntity bean) ;

    /**
     * search records from Database.
     */
    public List<InvoiceTypeEntity> searchLike(InvoiceTypeEntity bean) ;
    /**
     * Create a new record in Database.
     */
	public PrepayDetailEntity save(PrepayDetailEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public PrepayDetailEntity findPrepayDetailById(
		java.lang.Integer prepayDetailId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<PrepayDetailEntity> findPrepayDetailAll() ;
    
    /**
     * Update a record in Database.
     */
    public PrepayDetailEntity update(PrepayDetailEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deletePrepayDetail(
		java.lang.Integer prepayDetailId 

	) ;

    /**
     * search records from Database.
     */
    public List<PrepayDetailEntity> searchBy(PrepayDetailEntity bean) ;

    /**
     * search records from Database.
     */
    public List<PrepayDetailEntity> searchLike(PrepayDetailEntity bean) ;
    /**
     * Create a new record in Database.
     */
	public DeductDetailEntity save(DeductDetailEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public DeductDetailEntity findDeductDetailById(
		java.lang.Integer deductDetailId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<DeductDetailEntity> findDeductDetailAll() ;
    
    /**
     * Update a record in Database.
     */
    public DeductDetailEntity update(DeductDetailEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deleteDeductDetail(
		java.lang.Integer deductDetailId 

	) ;

    /**
     * search records from Database.
     */
    public List<DeductDetailEntity> searchBy(DeductDetailEntity bean) ;

    /**
     * search records from Database.
     */
    public List<DeductDetailEntity> searchLike(DeductDetailEntity bean) ;


    /**
     * Create a new record in Database.
     */
	public BillCycleEntity save(BillCycleEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public BillCycleEntity findBillCycleById(
		java.lang.Integer billId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<BillCycleEntity> findBillCycleAll() ;
    
    /**
     * Update a record in Database.
     */
    public BillCycleEntity update(BillCycleEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deleteBillCycle(
		java.lang.Integer billId 

	) ;

    /**
     * search records from Database.
     */
    public List<BillCycleEntity> searchBy(BillCycleEntity bean) ;

    /**
     * search records from Database.
     */
    public List<BillCycleEntity> searchLike(BillCycleEntity bean) ;
    /**
     * Create a new record in Database.
     */
	public LogParameterEntity save(LogParameterEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public LogParameterEntity findLogParameterById(
		java.lang.Integer parameterId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<LogParameterEntity> findLogParameterAll() ;
    
    /**
     * Update a record in Database.
     */
    public LogParameterEntity update(LogParameterEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deleteLogParameter(
		java.lang.Integer parameterId 

	) ;

    /**
     * search records from Database.
     */
    public List<LogParameterEntity> searchBy(LogParameterEntity bean) ;

    /**
     * search records from Database.
     */
    public List<LogParameterEntity> searchLike(LogParameterEntity bean) ;
    /**
     * Create a new record in Database.
     */
	public ChargeModeCycleAddEntity save(ChargeModeCycleAddEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public ChargeModeCycleAddEntity findChargeModeCycleAddById(
		java.lang.Integer additionId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<ChargeModeCycleAddEntity> findChargeModeCycleAddAll() ;
    
    /**
     * Update a record in Database.
     */
    public ChargeModeCycleAddEntity update(ChargeModeCycleAddEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deleteChargeModeCycleAdd(
		java.lang.Integer additionId 

	) ;

    /**
     * search records from Database.
     */
    public List<ChargeModeCycleAddEntity> searchBy(ChargeModeCycleAddEntity bean) ;

    /**
     * search records from Database.
     */
    public List<ChargeModeCycleAddEntity> searchLike(ChargeModeCycleAddEntity bean) ;
    /**
     * Create a new record in Database.
     */
	public DealerEntity save(DealerEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public DealerEntity findDealerById(
		java.lang.Integer dealerId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<DealerEntity> findDealerAll() ;
    
    /**
     * Update a record in Database.
     */
    public DealerEntity update(DealerEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deleteDealer(
		java.lang.Integer dealerId 

	) ;

    /**
     * search records from Database.
     */
    public List<DealerEntity> searchBy(DealerEntity bean) ;

    /**
     * search records from Database.
     */
    public List<DealerEntity> searchLike(DealerEntity bean) ;
    /**
     * Create a new record in Database.
     */
	public WarrantyEntity save(WarrantyEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public WarrantyEntity findWarrantyById(
		java.lang.Integer warrantyId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<WarrantyEntity> findWarrantyAll() ;
    
    /**
     * Update a record in Database.
     */
    public WarrantyEntity update(WarrantyEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deleteWarranty(
		java.lang.Integer warrantyId 

	) ;

    /**
     * search records from Database.
     */
    public List<WarrantyEntity> searchBy(WarrantyEntity bean) ;

    /**
     * search records from Database.
     */
    public List<WarrantyEntity> searchLike(WarrantyEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public CashMasterEntity save(CashMasterEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public CashMasterEntity findCashMasterById(
		java.lang.Integer cashMasterId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<CashMasterEntity> findCashMasterAll() ;
    
    /**
     * Update a record in Database.
     */
    public CashMasterEntity update(CashMasterEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deleteCashMaster(
		java.lang.Integer cashMasterId 

	) ;

    /**
     * search records from Database.
     */
    public List<CashMasterEntity> searchBy(CashMasterEntity bean) ;

    /**
     * search records from Database.
     */
    public List<CashMasterEntity> searchLike(CashMasterEntity bean) ;
    /**
     * Create a new record in Database.
     */
	public PrepayDeductMasterEntity save(PrepayDeductMasterEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public PrepayDeductMasterEntity findPrepayDeductMasterById(
		java.lang.Integer prepayDeductMasterId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<PrepayDeductMasterEntity> findPrepayDeductMasterAll() ;
    
    /**
     * Update a record in Database.
     */
    public PrepayDeductMasterEntity update(PrepayDeductMasterEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deletePrepayDeductMaster(
		java.lang.Integer prepayDeductMasterId 

	) ;

    /**
     * search records from Database.
     */
    public List<PrepayDeductMasterEntity> searchBy(PrepayDeductMasterEntity bean) ;

    /**
     * search records from Database.
     */
    public List<PrepayDeductMasterEntity> searchLike(PrepayDeductMasterEntity bean) ;
    /**
     * Create a new record in Database.
     */
	public UserEntity save(UserEntity bean) ;

    /**
     * Retrieve a record from Database.
     */
	public UserEntity findUserById(
		java.lang.Integer userId 

	) ;

    /**
     * Retrieve a record from Database.
     */
    public List<UserEntity> findUserAll() ;
    
    /**
     * Update a record in Database.
     */
    public UserEntity update(UserEntity bean) ;

    /**
     * Create a new record in Database.
     */
	public void deleteUser(
		java.lang.Integer userId 

	) ;

    /**
     * search records from Database.
     */
    public List<UserEntity> searchBy(UserEntity bean) ;

    /**
     * search records from Database.
     */
    public List<UserEntity> searchLike(UserEntity bean) ;
}
