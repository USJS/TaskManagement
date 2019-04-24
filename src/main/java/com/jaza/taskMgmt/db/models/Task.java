package com.jaza.taskMgmt.db.models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.jaza.taskMgmt.db.enm.Priority;

@Entity
public class Task implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "TASK_ID", updatable = false, unique = true, nullable = false)
	private Long taskId;
	
	@Column(name="TASK_NAME")
	private String taskName;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade={CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH})
	@JoinColumn(name = "LABEL_NAME", nullable = false)
	private Category category;
	
	@Column(name="PRIORITY")
	@Enumerated(EnumType.STRING)
	private Priority priority;
	
	@Column(name="DUE_DATE")
	private Long dueDate;
	
	@Column(name="TASK_CREATION_TIME")
	private Long taskCreationTime = System.currentTimeMillis();
	

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Long getDueDate() {
		return dueDate;
	}

	public void setDueDate(Long dueDate) {
		this.dueDate = dueDate;
	}

	public Long getTaskCreationTime() {
		return taskCreationTime;
	}

	public void setTaskCreationTime(Long taskCreationTime) {
		this.taskCreationTime = taskCreationTime;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

}
