package com.rohitmunde.trustdesk.repository;

import com.rohitmunde.trustdesk.entity.AiOperationTrace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiOperationTraceRepository extends JpaRepository<AiOperationTrace, Long> {

    List<AiOperationTrace> findByTicketIdOrderByCreatedAtDesc(String ticketId);
}
