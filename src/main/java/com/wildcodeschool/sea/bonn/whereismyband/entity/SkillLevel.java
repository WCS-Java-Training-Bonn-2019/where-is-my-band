// Diese Klasse wird im MVP wahrscheinlich nicht verwendet (MIK 22.01.2020)

package com.wildcodeschool.sea.bonn.whereismyband.entity;

public class SkillLevel {

	// database id
	private Long id;
	private String name;

	public SkillLevel(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "SkillLevel [id=" + id + ", name=" + name + "]";
	}
	
}
