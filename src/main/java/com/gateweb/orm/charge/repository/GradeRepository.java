/*
 * $Header: $
 * This java source file is generated by pkliu on Tue Jan 30 14:38:14 CST 2018
 * For more information, please contact pkliu@sysfoundry.com
 */
package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author pkliu
 * <p>
 * This class provides methods to populate DB Table of Grade
 */
@Repository("grade")
public interface GradeRepository extends JpaRepository<Grade, Integer>
        , QuerydslPredicateExecutor<Grade> {

	/*public GradeEntity findByGradeId(Long gradeId);
	
	public Page<GradeEntity> findByGradeId(Long gradeId, Pageable pageable);
	
	public boolean exists(Long gradeId);
	
	public List<GradeEntity> findAll();
	
	public List<GradeEntity> findTop100ByGradeId(Long gradeId);
	
	public long count();*/

    //取得某級距方案的級距清單
    public List<Grade> findByChargeId(Integer chargeId);

    Grade findByGradeId(Integer gradeId);

}

	