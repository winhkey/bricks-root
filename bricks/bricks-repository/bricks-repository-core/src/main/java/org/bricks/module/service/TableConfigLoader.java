package org.bricks.module.service;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static org.bricks.utils.ReflectionUtils.addDeclaredFields;
import static org.bricks.utils.ReflectionUtils.addDeclaredMethods;
import static org.bricks.utils.RegexUtils.matches;
import static org.bricks.utils.RegexUtils.regularGroup;
import static org.bricks.utils.StringUtils.firstToLowercase;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.MapUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import org.bricks.exception.BaseException;
import org.bricks.module.annotation.Excel;
import org.bricks.module.annotation.ExcelColumn;
import org.bricks.module.annotation.Unique;
import org.bricks.module.bean.ColumnConfig;
import org.bricks.module.bean.TableConfig;
import org.bricks.module.enums.DataType;
import org.bricks.pattern.PatternService;

/**
 * 加载表配置
 *
 * @author fuzy
 *
 */
@Service
public class TableConfigLoader
{

    /**
     * 缓存
     */
    private static final Map<String, TableConfig> CACHE = newHashMap();

    /**
     * getter setter正则
     */
    private Pattern getSetterPattern;

    /**
     * 正则配置
     */
    @Resource
    private PatternService patternService;

    /**
     * 初始化
     */
    @PostConstruct
    public void init()
    {
        getSetterPattern = patternService.loadPatternById("get-setter");
    }

    /**
     * 根据类加载表配置
     *
     * @param clazz 类
     * @return 表配置
     */
    public TableConfig load(Class<?> clazz)
    {
        if (CACHE.containsKey(clazz.getName()))
        {
            return CACHE.get(clazz.getName());
        }
        List<Field> fieldList = newArrayList();
        addDeclaredFields(clazz, fieldList, true, false);
        List<Method> methodList = newArrayList();
        addDeclaredMethods(clazz, methodList, true, false);
        Map<String, Field> map = fieldList.stream()
                .collect(toMap(Field::getName, o -> o));
        Stream<Method> methodStream = methodList.stream()
                .filter(m -> m.getDeclaredAnnotation(ExcelColumn.class) != null)
                .filter(m ->
                {
                    String name = m.getName();
                    if (!matches(getSetterPattern, name))
                    {
                        throw new BaseException("@ExcelColumn only add with Field or Getter/Setter");
                    }
                    if (map.get(firstToLowercase(regularGroup(getSetterPattern, name, 2))) == null)
                    {
                        throw new BaseException("@ExcelColumn only add with Field or Getter/Setter");
                    }
                    return true;
                })
                .distinct();
        Map<Member, ExcelColumn> excelColumnMap = concat(fieldList.stream()
                .filter(f -> f.getDeclaredAnnotation(ExcelColumn.class) != null), methodStream)
                        .collect(toMap(o -> o, o -> o.getDeclaredAnnotation(ExcelColumn.class)));
        map.clear();
        if (isEmpty(excelColumnMap))
        {
            return null;
        }
        List<String> fList = newArrayList();
        Map<Integer, ColumnConfig> columnMap = newLinkedHashMap();
        Map<String, String> fieldTitleMap = newLinkedHashMap();
        Map<String, DataType> fieldDataTypeMap = newLinkedHashMap();
        TableConfig tableConfig = new TableConfig().setEntityClass(clazz)
                .setStartRow(1)
                .setColumnMap(columnMap)
                .setFieldList(fList)
                .setFieldTitleMap(fieldTitleMap)
                .setFieldDataTypeMap(fieldDataTypeMap);
        excelColumnMap.entrySet()
                .forEach(entry -> dealField(fList, columnMap, fieldTitleMap, fieldDataTypeMap, tableConfig, entry));
        ofNullable(clazz.getAnnotation(Excel.class)).ifPresent(excel -> tableConfig.setStartRow(excel.startRow())
                .setUniques(of(excel.uniques()).map(Unique::columns)
                        .filter(ArrayUtils::isNotEmpty)
                        .collect(toList()))
                .setFilterName(excel.filterName())
                .setFilterClass(excel.filterClass()));
        if (isNotEmpty(tableConfig.getUniques()))
        {
            tableConfig.setUnique(true);
        }
        CACHE.put(clazz.getName(), tableConfig);
        return tableConfig;
    }

    private void dealField(List<String> fList, Map<Integer, ColumnConfig> columnMap, Map<String, String> fieldTitleMap,
            Map<String, DataType> fieldDataTypeMap, TableConfig tableConfig, Entry<Member, ExcelColumn> entry)
    {
        ExcelColumn excelColumn = entry.getValue();
        ColumnConfig cfg = new ColumnConfig();
        if (excelColumn.column() > -1)
        {
            cfg.setColumn(excelColumn.column());
            columnMap.put(cfg.getColumn(), cfg);
        }
        String name = entry.getKey()
                .getName();
        if (matches(getSetterPattern, name))
        {
            name = firstToLowercase(regularGroup(getSetterPattern, name, 2));
        }
        cfg.setField(isNotBlank(excelColumn.field()) ? excelColumn.field() : name)
                .setTitle(isNotBlank(excelColumn.title()) ? excelColumn.title() : name)
                .setDataType(excelColumn.dataType())
                .setFormat(excelColumn.format())
                .setFilterName(excelColumn.filterName())
                .setFilterClass(excelColumn.filterClass())
                .setMandatory(excelColumn.mandatory())
                .setMaxLength(excelColumn.maxLength())
                .setRegex(excelColumn.regex())
                .setUnique(excelColumn.unique());
        fList.add(cfg.getField());
        fieldTitleMap.put(cfg.getField(), cfg.getTitle());
        fieldDataTypeMap.put(cfg.getField(), cfg.getDataType());
        if (cfg.isUnique())
        {
            tableConfig.setUnique(true);
        }
    }

}
