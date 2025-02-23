package com.zayaanit.aspi.service.impl;

import org.springframework.stereotype.Service;

/**
 * @author Zubayer Ahamed
 * @since Jul 27, 2023
 */
@Service
public class DBBackupService extends AbstractGenericService {

	public int performBackup(String dbName, String backupFilePath) throws Exception {
		String sql = "BACKUP DATABASE " + dbName + " TO DISK = ? WITH COMPRESSION";
		return jdbcTemplate.update(sql, backupFilePath);
	}
}
