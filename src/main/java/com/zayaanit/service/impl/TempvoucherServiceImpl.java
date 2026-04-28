package com.zayaanit.service.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Service;

import com.zayaanit.entity.Tempvoucher;
import com.zayaanit.service.TempvoucherService;

/**
 * @author Zubayer Ahaned
 * @since Feb 26, 2025
 * @contact +8801748562164
 * @email zubayerahamed1990@gmail.com
 * @website https://www.zubayerahamed.com
 */
@Service
public class TempvoucherServiceImpl extends AbstractGenericService implements TempvoucherService {

	@Override
	public void batchUpdateTempvouchers(List<Tempvoucher> tempvouchers) {
		String sql = "UPDATE tempvoucher SET Status = ?, ErrorDetails = ? WHERE zid = ? AND xrow = ?";

		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@SuppressWarnings("null")
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Tempvoucher temp = tempvouchers.get(i);
				ps.setInt(1, temp.getAllOk() ? 1 : 0); // Convert boolean to int
				ps.setString(2, temp.getErrorDetails());
				ps.setInt(3, temp.getZid());
				ps.setInt(4, temp.getXrow());
			}

			@Override
			public int getBatchSize() {
				return tempvouchers.size();
			}

		});
	}

}
