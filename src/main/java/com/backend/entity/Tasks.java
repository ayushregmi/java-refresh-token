package com.backend.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Tasks {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="task_id", nullable=false)
	private int taskId;
	@Column(nullable=false)
	private String title;
	@Column(nullable=false)
	private String description;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Column(name="created_at")
	private Date createdAt;

	private Date deadline;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="assigner_id", referencedColumnName="user_id", nullable=false)
	private Employer assigner;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="assignee_id", referencedColumnName="user_id", nullable=false)
	private Employee assignee;
	
}
