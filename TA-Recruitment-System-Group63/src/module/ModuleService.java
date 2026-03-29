package module;

import common.FileUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * ModuleService - 模块管理模块
 * 开发者：郭骜之 (Guo Aozhi)
 */
public class ModuleService {
    private static final String FILE_NAME = "modules.csv";

    /**
     * 创建新模块
     * @param moduleCode 模块代码
     * @param moduleName 模块名称
     * @param moID 绑定的 MO 账号
     * @param description 模块描述
     * @return 是否成功
     */
    public boolean createModule(String moduleCode, String moduleName, String moID, String description) {
        if (moduleCode == null || moduleCode.trim().isEmpty()) {
            System.out.println("Error: Module Code cannot be empty.");
            return false;
        }

        if (getModuleByCode(moduleCode) != null) {
            System.out.println("Error: Module with code " + moduleCode + " already exists.");
            return false;
        }

        String[] moduleData = {moduleCode, moduleName, moID, description};
        List<String[]> dataList = new ArrayList<>();
        dataList.add(moduleData);

        return FileUtil.writeCSV(FILE_NAME, dataList, true);
    }

    /**
     * 根据代码获取模块
     * @param moduleCode 模块代码
     * @return 模块数据
     */
    public String[] getModuleByCode(String moduleCode) {
        List<String[]> allModules = FileUtil.readCSV(FILE_NAME);
        for (String[] row : allModules) {
            if (row.length > 0 && row[0].equalsIgnoreCase(moduleCode)) {
                return row;
            }
        }
        return null;
    }

    /**
     * 获取全部模块
     */
    public List<String[]> getAllModules() {
        return FileUtil.readCSV(FILE_NAME);
    }

    /**
     * 更新模块信息
     * @param moduleCode 模块代码
     * @param newData 新数据
     * @return 是否成功
     */
    public boolean updateModule(String moduleCode, String[] newData) {
        List<String[]> allModules = FileUtil.readCSV(FILE_NAME);
        boolean found = false;
        
        for (int i = 0; i < allModules.size(); i++) {
            String[] row = allModules.get(i);
            if (row.length > 0 && row[0].equalsIgnoreCase(moduleCode)) {
                allModules.set(i, newData);
                found = true;
                break;
            }
        }

        if (found) {
            return FileUtil.writeCSV(FILE_NAME, allModules, false);
        }
        return false;
    }

    /**
     * 绑定 MO 账号
     * @param moduleCode 模块代码
     * @param moID MO账号
     */
    public boolean bindMO(String moduleCode, String moID) {
        String[] module = getModuleByCode(moduleCode);
        if (module != null) {
            module[2] = moID; 
            return updateModule(moduleCode, module);
        }
        System.out.println("Error: Module not found.");
        return false;
    }
}
