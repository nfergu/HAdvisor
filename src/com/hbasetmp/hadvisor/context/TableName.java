package com.hbasetmp.hadvisor.context;

import java.util.Arrays;

public class TableName {

	private final byte[] asBytes;
	
	public TableName(byte[] tableNameAsBytes) {
		this.asBytes = tableNameAsBytes;
	}

	public byte[] getAsBytes() {
		return asBytes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(asBytes);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableName other = (TableName) obj;
		if (!Arrays.equals(asBytes, other.asBytes))
			return false;
		return true;
	}
	
}
