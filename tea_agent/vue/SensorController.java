package com.wumei.smart.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.wumei.smart.domain.SensorData;
import com.wumei.smart.domain.dto.SensorDataDTO;
import com.wumei.smart.service.ISensorDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 传感器数据Controller
 * 
 * @author wumei
 * @date 2024-01-15
 */
@RestController
@RequestMapping("/sensor")
public class SensorController extends BaseController {
    @Autowired
    private ISensorDataService sensorDataService;

    /**
     * 获取实时传感器数据
     */
    @GetMapping("/current")
    public AjaxResult getCurrentData() {
        try {
            SensorDataDTO data = sensorDataService.getCurrentSensorData();
            return AjaxResult.success(data);
        } catch (Exception e) {
            logger.error("获取实时传感器数据失败", e);
            return AjaxResult.error("获取数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取传感器历史数据
     */
    @GetMapping("/history")
    public AjaxResult getHistoryData(@RequestParam(defaultValue = "24") Integer hours) {
        try {
            SensorDataDTO data = sensorDataService.getSensorHistoryData(hours);
            return AjaxResult.success(data);
        } catch (Exception e) {
            logger.error("获取传感器历史数据失败", e);
            return AjaxResult.error("获取历史数据失败：" + e.getMessage());
        }
    }

    /**
     * 获取传感器统计数据
     */
    @GetMapping("/stats")
    public AjaxResult getStatsData() {
        try {
            SensorDataDTO data = sensorDataService.getSensorStatsData();
            return AjaxResult.success(data);
        } catch (Exception e) {
            logger.error("获取传感器统计数据失败", e);
            return AjaxResult.error("获取统计数据失败：" + e.getMessage());
        }
    }

    /**
     * 查询传感器数据列表
     */
    @PreAuthorize("@ss.hasPermi('sensor:data:list')")
    @GetMapping("/list")
    public TableDataInfo list(SensorData sensorData) {
        startPage();
        List<SensorData> list = sensorDataService.selectSensorDataList(sensorData);
        return getDataTable(list);
    }

    /**
     * 导出传感器数据列表
     */
    @PreAuthorize("@ss.hasPermi('sensor:data:export')")
    @Log(title = "传感器数据", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SensorData sensorData) {
        List<SensorData> list = sensorDataService.selectSensorDataList(sensorData);
        ExcelUtil<SensorData> util = new ExcelUtil<SensorData>(SensorData.class);
        util.exportExcel(response, list, "传感器数据数据");
    }

    /**
     * 获取传感器数据详细信息
     */
    @PreAuthorize("@ss.hasPermi('sensor:data:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sensorDataService.selectSensorDataById(id));
    }

    /**
     * 新增传感器数据
     */
    @PreAuthorize("@ss.hasPermi('sensor:data:add')")
    @Log(title = "传感器数据", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SensorData sensorData) {
        return toAjax(sensorDataService.insertSensorData(sensorData));
    }

    /**
     * 修改传感器数据
     */
    @PreAuthorize("@ss.hasPermi('sensor:data:edit')")
    @Log(title = "传感器数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SensorData sensorData) {
        return toAjax(sensorDataService.updateSensorData(sensorData));
    }

    /**
     * 删除传感器数据
     */
    @PreAuthorize("@ss.hasPermi('sensor:data:remove')")
    @Log(title = "传感器数据", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sensorDataService.deleteSensorDataByIds(ids));
    }
}