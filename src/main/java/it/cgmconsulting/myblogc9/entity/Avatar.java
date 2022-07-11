package it.cgmconsulting.myblogc9.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Avatar {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable=false, length=20)
	private String fileType;
	
	@Column(nullable=false, length=20)
	private String fileName;
	
	@Lob // campo su cui salvare il contenuto del file sotto forma di array di byte
	@Column(nullable=false)
	private byte[] data;

	public Avatar(String fileType, String fileName, byte[] data) {
		super();
		this.fileType = fileType;
		this.fileName = fileName;
		this.data = data;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Avatar other = (Avatar) obj;
		return id == other.id;
	}
	
	

}
