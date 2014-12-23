package edu.pku.func;

public class TaskBranch {
	
	private String taskName;
	private String taskContextValue;

	public TaskBranch(String taskName, String taskContextValue) {
		
		this.taskName = taskName;
		this.taskContextValue = taskContextValue;
	}

	public String getTaskName() {
		
		return taskName;
	}

	public void setTaskName(String taskName) {
		
		this.taskName = taskName;
	}

	public String getTaskContextValue() {
		
		return taskContextValue;
	}

	public void setTaskContextValue(String taskContextValue) {
		
		this.taskContextValue = taskContextValue;
	}
}