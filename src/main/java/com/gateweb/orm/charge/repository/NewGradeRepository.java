package com.gateweb.orm.charge.repository;

import com.gateweb.orm.charge.entity.NewGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface NewGradeRepository extends JpaRepository<NewGrade, Long>
        , QuerydslPredicateExecutor<NewGrade> {

    Optional<NewGrade> findByGradeIdIsAndRootIdIs(Long gradeId, Long rootId);

    Optional<NewGrade> findByGradeId(Long gradeId);

    List<NewGrade> findByRootIdIsAndGradeIdIsNotOrderByLevel(Long rootId, Long gradeId);

    Collection<NewGrade> findByRootIdIs(Long rootId);

    List<NewGrade> findByRootIdOrderByLevelAsc(Long rootId);

    @Query(
            value = " SELECT * " +
                    " FROM new_grade " +
                    " WHERE root_id = grade_id " +
                    " AND level = 0 ",
            nativeQuery = true)
    List<NewGrade> findRootGradeList();
}
