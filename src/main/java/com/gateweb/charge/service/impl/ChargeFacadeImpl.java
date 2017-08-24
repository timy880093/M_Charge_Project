/*
 * $Header: $
 * This java source file is generated by pkliu on Thu Aug 03 11:22:57 CST 2017
 * For more information, please contact pkliu@meshinnovation.com
 */
package com.gateweb.charge.service.impl;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.gateweb.charge.exception.ChargeSysException;
import com.gateweb.charge.model.Company;
import com.gateweb.charge.model.InvoiceDetails;
import com.gateweb.charge.model.InvoiceMain;
import com.gateweb.charge.model.User;
import com.gateweb.charge.repository.Company2Repository;
import com.gateweb.charge.repository.InvoiceDetails2Repository;
import com.gateweb.charge.repository.InvoiceMain2Repository;
import com.gateweb.charge.repository.User2Repository;
import com.gateweb.charge.service.ChargeFacade;
/**
 * @author pkliu
 *
 */
@Service("chargeFacade") 
public class ChargeFacadeImpl implements ChargeFacade {

	/**
	* <p><code>Log</code> instance for this application. </p>
	*/
	private Log log = LogFactory.getLog(this.getClass().getName());
	
	
	/**
	 * InvoiceMainRepository
	 */
	@Resource
	private InvoiceMain2Repository invoiceMain2Repository = null;
	
	
	/**
	 * CompanyRepository
	 */
	@Autowired
	private Company2Repository company2Repository = null;
	
	
	/**
	 * UserRepository
	 */
	@Autowired
	private User2Repository user2Repository = null;
	
	
	/**
	 * InvoiceDetailsRepository
	 */
	@Autowired
	private InvoiceDetails2Repository invoiceDetails2Repository = null;
	


	/**
	 *
	 */
	public void setInvoiceMainRepository(InvoiceMain2Repository setRepository){
		this.invoiceMain2Repository = setRepository;
	}
	
	/*
	public InvoiceMainRepository getInvoiceMainRepository(){
		return this.invoiceMain2Repository;
	}	
	 */
	/**
	 *
	 */
	public void setCompanyRepository(Company2Repository setRepository){
		this.company2Repository = setRepository;
	}
	
	/*
	public CompanyRepository getCompanyRepository(){
		return this.company2Repository;
	}	
	 */
	/**
	 *
	 */
	public void setUserRepository(User2Repository setRepository){
		this.user2Repository = setRepository;
	}
	
	/*
	public UserRepository getUserRepository(){
		return this.user2Repository;
	}	
	 */
	/**
	 *
	 */
	public void setInvoiceDetailsRepository(InvoiceDetails2Repository setRepository){
		this.invoiceDetails2Repository = setRepository;
	}
	
	/*
	public InvoiceDetailsRepository getInvoiceDetailsRepository(){
		return this.invoiceDetails2Repository;
	}	
	 */
	/**
	 *
	 *
	 */
	public ChargeFacadeImpl(){
	}

    /**
     * Create a new record in Database.
     * @exception    ChargeSysException if something is wrong.
     */
	public InvoiceMain save(InvoiceMain bean) {

		log.debug("ChargeFacadeImpl create InvoiceMain  before dao save InvoiceMain :  "+bean);	
		return invoiceMain2Repository.save(bean); 
		//log.debug("ChargeFacadeImpl create InvoiceMain   successfully.");		
	}

    /**
     * Retrieve a record from Database.
     * @param	id
     */
	public InvoiceMain findInvoiceMainById(
		java.lang.Long invoiceId 
	) {
		InvoiceMain data = null;
			log.debug("ChargeFacadeImpl findInvoiceMainById   findInvoiceMainById :" 
				+"invoiceId = "+ invoiceId
			);	
			data = invoiceMain2Repository.findOne(
				invoiceId 
			);			
		log.debug("ChargeFacadeImpl searchInvoiceMainByKey   successfully.");	
		return data;
	}

    /**
     * Retrieve all records from Database.
     * @exception       ChargeSysException if something is wrong.
     */
    public List<InvoiceMain> findInvoiceMainAll() {
		log.debug("ChargeFacadeImpl searchInvoiceMainAll  before dao get all ");	

		List<InvoiceMain> results = null;
		results = invoiceMain2Repository.findAll();
		log.debug("ChargeFacadeImpl searchInvoiceMainAll   successfully.");	
		return results;
    }

    /**
     * Update a record in Database.
     * @param bean   The Object to be saved.
     * @exception    ChargeSysException if something is wrong.
     */
    public InvoiceMain update(InvoiceMain bean) {
		log.debug("ChargeFacadeImpl update InvoiceMain   before dao update : bean "+bean);	
		return invoiceMain2Repository.save(bean);
		//log.debug("ChargeFacadeImpl update InvoiceMain    successfully.");	

	}

    /**
     * Delete a new record in Database.
     */
	public void deleteInvoiceMain(
		java.lang.Long invoiceId 
	) {

			log.debug("ChargeFacadeImpl deleteInvoiceMain   delete :" 
				+"invoiceId = "+ invoiceId
			);			
			invoiceMain2Repository.delete(
				invoiceId 
			);
			 
		log.debug("ChargeFacadeImpl deleteInvoiceMain   successfully. ");	
	} 

    /**
     * search records from Database.
     * @param bean   The Object search criteria
     */
    public List<InvoiceMain> searchBy(InvoiceMain bean) {
		log.debug("ChargeFacadeImpl searchWith InvoiceMain   before dao searchWith : bean "+bean);	
		List<InvoiceMain> results = null;
		results = invoiceMain2Repository.searchWithVo(bean);	
		log.debug("ChargeFacadeImpl searchWithInvoiceMain   successfully. ");	
		return results;
    }

    /**
     * search records from Database.
     * @param bean   The Object search criteria
     */
    public List<InvoiceMain> searchLike(InvoiceMain bean) {
		log.debug("ChargeFacadeImpl searchLike InvoiceMain   before dao searchLike : bean "+bean);	
		List<InvoiceMain> results = null;
		results = invoiceMain2Repository.searchLikeVo(bean);
		log.debug("ChargeFacadeImpl searchLikeInvoiceMain   successfully. ");	
		return results;
    }
    /**
     * Create a new record in Database.
     * @exception    ChargeSysException if something is wrong.
     */
	public Company save(Company bean) {

		log.debug("ChargeFacadeImpl create Company  before dao save Company :  "+bean);	
		return company2Repository.save(bean); 
		//log.debug("ChargeFacadeImpl create Company   successfully.");		
	}

    /**
     * Retrieve a record from Database.
     * @param	id
     */
	public Company findCompanyById(
		java.lang.Long companyId 
	) {
		Company data = null;
			log.debug("ChargeFacadeImpl findCompanyById   findCompanyById :" 
				+"companyId = "+ companyId
			);	
			data = company2Repository.findOne(
				companyId 
			);			
		log.debug("ChargeFacadeImpl searchCompanyByKey   successfully.");	
		return data;
	}

    /**
     * Retrieve all records from Database.
     * @exception       ChargeSysException if something is wrong.
     */
    public List<Company> findCompanyAll() {
		log.debug("ChargeFacadeImpl searchCompanyAll  before dao get all ");	

		List<Company> results = null;
		results = company2Repository.findAll();
		log.debug("ChargeFacadeImpl searchCompanyAll   successfully.");	
		return results;
    }

    /**
     * Update a record in Database.
     * @param bean   The Object to be saved.
     * @exception    ChargeSysException if something is wrong.
     */
    public Company update(Company bean) {
		log.debug("ChargeFacadeImpl update Company   before dao update : bean "+bean);	
		return company2Repository.save(bean);
		//log.debug("ChargeFacadeImpl update Company    successfully.");	

	}

    /**
     * Delete a new record in Database.
     */
	public void deleteCompany(
		java.lang.Long companyId 
	) {

			log.debug("ChargeFacadeImpl deleteCompany   delete :" 
				+"companyId = "+ companyId
			);			
			company2Repository.delete(
				companyId 
			);
			 
		log.debug("ChargeFacadeImpl deleteCompany   successfully. ");	
	} 

    /**
     * search records from Database.
     * @param bean   The Object search criteria
     */
    public List<Company> searchBy(Company bean) {
		log.debug("ChargeFacadeImpl searchWith Company   before dao searchWith : bean "+bean);	
		List<Company> results = null;
		results = company2Repository.searchWithVo(bean);	
		log.debug("ChargeFacadeImpl searchWithCompany   successfully. ");	
		return results;
    }

    /**
     * search records from Database.
     * @param bean   The Object search criteria
     */
    public List<Company> searchLike(Company bean) {
		log.debug("ChargeFacadeImpl searchLike Company   before dao searchLike : bean "+bean);	
		List<Company> results = null;
		results = company2Repository.searchLikeVo(bean);
		log.debug("ChargeFacadeImpl searchLikeCompany   successfully. ");	
		return results;
    }
    /**
     * Create a new record in Database.
     * @exception    ChargeSysException if something is wrong.
     */
	public User save(User bean) {

		log.debug("ChargeFacadeImpl create User  before dao save User :  "+bean);	
		return user2Repository.save(bean); 
		//log.debug("ChargeFacadeImpl create User   successfully.");		
	}

    /**
     * Retrieve a record from Database.
     * @param	id
     */
	public User findUserById(
		java.lang.Long userId 
	) {
		User data = null;
			log.debug("ChargeFacadeImpl findUserById   findUserById :" 
				+"userId = "+ userId
			);	
			data = user2Repository.findOne(
				userId 
			);			
		log.debug("ChargeFacadeImpl searchUserByKey   successfully.");	
		return data;
	}

    /**
     * Retrieve all records from Database.
     * @exception       ChargeSysException if something is wrong.
     */
    public List<User> findUserAll() {
		log.debug("ChargeFacadeImpl searchUserAll  before dao get all ");	

		List<User> results = null;
		results = user2Repository.findAll();
		log.debug("ChargeFacadeImpl searchUserAll   successfully.");	
		return results;
    }

    /**
     * Update a record in Database.
     * @param bean   The Object to be saved.
     * @exception    ChargeSysException if something is wrong.
     */
    public User update(User bean) {
		log.debug("ChargeFacadeImpl update User   before dao update : bean "+bean);	
		return user2Repository.save(bean);
		//log.debug("ChargeFacadeImpl update User    successfully.");	

	}

    /**
     * Delete a new record in Database.
     */
	public void deleteUser(
		java.lang.Long userId 
	) {

			log.debug("ChargeFacadeImpl deleteUser   delete :" 
				+"userId = "+ userId
			);			
			user2Repository.delete(
				userId 
			);
			 
		log.debug("ChargeFacadeImpl deleteUser   successfully. ");	
	} 

    /**
     * search records from Database.
     * @param bean   The Object search criteria
     */
    public List<User> searchBy(User bean) {
		log.debug("ChargeFacadeImpl searchWith User   before dao searchWith : bean "+bean);	
		List<User> results = null;
		results = user2Repository.searchWithVo(bean);	
		log.debug("ChargeFacadeImpl searchWithUser   successfully. ");	
		return results;
    }

    /**
     * search records from Database.
     * @param bean   The Object search criteria
     */
    public List<User> searchLike(User bean) {
		log.debug("ChargeFacadeImpl searchLike User   before dao searchLike : bean "+bean);	
		List<User> results = null;
		results = user2Repository.searchLikeVo(bean);
		log.debug("ChargeFacadeImpl searchLikeUser   successfully. ");	
		return results;
    }

    /**
     * Create a new record in Database.
     * @exception    ChargeSysException if something is wrong.
     */
	public InvoiceDetails save(InvoiceDetails bean) {

		log.debug("ChargeFacadeImpl create InvoiceDetails  before dao save InvoiceDetails :  "+bean);	
		return invoiceDetails2Repository.save(bean); 
		//log.debug("ChargeFacadeImpl create InvoiceDetails   successfully.");		
	}

    /**
     * Retrieve a record from Database.
     * @param	id
     */
	public InvoiceDetails findInvoiceDetailsById(
		java.lang.Long invoiceDetailsId 
	) {
		InvoiceDetails data = null;
			log.debug("ChargeFacadeImpl findInvoiceDetailsById   findInvoiceDetailsById :" 
				+"invoiceDetailsId = "+ invoiceDetailsId
			);	
			data = invoiceDetails2Repository.findOne(
				invoiceDetailsId 
			);			
		log.debug("ChargeFacadeImpl searchInvoiceDetailsByKey   successfully.");	
		return data;
	}

    /**
     * Retrieve all records from Database.
     * @exception       ChargeSysException if something is wrong.
     */
    public List<InvoiceDetails> findInvoiceDetailsAll() {
		log.debug("ChargeFacadeImpl searchInvoiceDetailsAll  before dao get all ");	

		List<InvoiceDetails> results = null;
		results = invoiceDetails2Repository.findAll();
		log.debug("ChargeFacadeImpl searchInvoiceDetailsAll   successfully.");	
		return results;
    }

    /**
     * Update a record in Database.
     * @param bean   The Object to be saved.
     * @exception    ChargeSysException if something is wrong.
     */
    public InvoiceDetails update(InvoiceDetails bean) {
		log.debug("ChargeFacadeImpl update InvoiceDetails   before dao update : bean "+bean);	
		return invoiceDetails2Repository.save(bean);
		//log.debug("ChargeFacadeImpl update InvoiceDetails    successfully.");	

	}

    /**
     * Delete a new record in Database.
     */
	public void deleteInvoiceDetails(
		java.lang.Long invoiceDetailsId 
	) {

			log.debug("ChargeFacadeImpl deleteInvoiceDetails   delete :" 
				+"invoiceDetailsId = "+ invoiceDetailsId
			);			
			invoiceDetails2Repository.delete(
				invoiceDetailsId 
			);
			 
		log.debug("ChargeFacadeImpl deleteInvoiceDetails   successfully. ");	
	} 

    /**
     * search records from Database.
     * @param bean   The Object search criteria
     */
    public List<InvoiceDetails> searchBy(InvoiceDetails bean) {
		log.debug("ChargeFacadeImpl searchWith InvoiceDetails   before dao searchWith : bean "+bean);	
		List<InvoiceDetails> results = null;
		results = invoiceDetails2Repository.searchWithVo(bean);	
		log.debug("ChargeFacadeImpl searchWithInvoiceDetails   successfully. ");	
		return results;
    }

    /**
     * search records from Database.
     * @param bean   The Object search criteria
     */
    public List<InvoiceDetails> searchLike(InvoiceDetails bean) {
		log.debug("ChargeFacadeImpl searchLike InvoiceDetails   before dao searchLike : bean "+bean);	
		List<InvoiceDetails> results = null;
		results = invoiceDetails2Repository.searchLikeVo(bean);
		log.debug("ChargeFacadeImpl searchLikeInvoiceDetails   successfully. ");	
		return results;
    }
    
 
}
