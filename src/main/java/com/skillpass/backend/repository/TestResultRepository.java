package com.skillpass.backend.repository;

import com.skillpass.backend.entity.TestResult;
import com.skillpass.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestResultRepository extends JpaRepository<TestResult, Long> {

    List<TestResult> findByUserId(Long userId);

    // Meilleurs scores pour un test
    List<TestResult> findByTestIdOrderByScoreDesc(Long testId);

    // Résultats récents
    List<TestResult> findTop10ByOrderByCompletedAtDesc();
}