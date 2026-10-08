package com.homeservice.support;

import com.baomidou.mybatisplus.annotation.TableName;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/** 从唯一初始化 SQL 读取预期结构；从不执行 SQL 文件。 */
public final class SqlBaseline {
    public static final Path SQL = Path.of("../database/init/home_service.sql");
    public record Column(String name,String definition,String type,boolean nullable,boolean auto,String defaultValue,String collation) {}
    public record Table(String name,Map<String,Column> columns,Map<String,List<String>> indexes,Map<String,String> checks,String primaryKey) {}
    public static Map<String,Table> read() throws Exception {
        String sql=Files.readString(SQL);Map<String,Table> tables=new LinkedHashMap<>();
        Matcher matcher=Pattern.compile("CREATE TABLE (\\w+) \\((.*?)\\n\\) ENGINE",Pattern.DOTALL).matcher(sql);
        while(matcher.find()) {
            String name=matcher.group(1),body=matcher.group(2);Map<String,Column> columns=new LinkedHashMap<>();
            Matcher c=Pattern.compile("^    (\\w+) ((?:BIGINT|INT|SMALLINT|TINYINT|VARCHAR|CHAR|DECIMAL|DATETIME|TIME|JSON)\\b.*)",Pattern.MULTILINE).matcher(body);
            while(c.find()) {
                String def=c.group(2);Matcher type=Pattern.compile("\\w+(?:\\([0-9,]+\\))?(?: UNSIGNED)?").matcher(def);type.find();
                Matcher defaults=Pattern.compile(" DEFAULT ('[^']*'|CURRENT_TIMESTAMP|[0-9.]+)").matcher(def);
                String defaultValue=defaults.find()?defaults.group(1).replace("'",""):null;
                String collation=def.contains("ascii_bin")?"ascii_bin":def.startsWith("VARCHAR")||def.startsWith("CHAR")?"utf8mb4_0900_ai_ci":null;
                columns.put(c.group(1),new Column(c.group(1),def,type.group().toLowerCase(Locale.ROOT),!def.contains("NOT NULL"),def.contains("AUTO_INCREMENT"),defaultValue,collation));
            }
            Matcher primary=Pattern.compile("PRIMARY KEY \\((\\w+)\\)").matcher(body);primary.find();
            Map<String,List<String>> indexes=new TreeMap<>();indexes.put("PRIMARY",List.of(primary.group(1)));
            Matcher unique=Pattern.compile("UNIQUE KEY (\\w+) \\(([^)]+)\\)").matcher(body);
            while(unique.find()) indexes.put(unique.group(1),Arrays.stream(unique.group(2).split(",")).map(String::strip).toList());
            Map<String,String> checks=new TreeMap<>();Matcher check=Pattern.compile("CONSTRAINT (\\w+) CHECK \\((.*)\\),?$",Pattern.MULTILINE).matcher(body);
            while(check.find()) checks.put(check.group(1),check.group(2));
            tables.put(name,new Table(name,columns,indexes,checks,primary.group(1)));
        }
        return tables;
    }
    public static Map<String,Class<?>> entities() throws Exception {
        var scanner=new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(TableName.class));
        Map<String,Class<?>> result=new TreeMap<>();
        for(var definition:scanner.findCandidateComponents("com.homeservice.domain.po")) {
            Class<?> type=Class.forName(definition.getBeanClassName());result.put(type.getAnnotation(TableName.class).value(),type);
        }
        return result;
    }
    public static String normalize(String expression) {
        if(expression==null)return "";
        return expression.replace("`","").replace("\\'","'")
            .replaceAll("(?i)_(ascii|utf8mb4|utf8mb3)\\s*", "")
            .replaceAll("(?i)REGEXP_LIKE\\((\\w+),\\s*('[^']*')\\)","$1 REGEXP $2")
            .replaceAll("(?i)MOD\\((\\w+),\\s*(\\d+)\\)","$1 % $2")
            .replaceAll("(?i)DATE_ADD\\((\\w+),\\s*(INTERVAL \\d+ \\w+)\\)","$1 + $2")
            .replace("!=","<>").replaceAll("[\\s()]","").toLowerCase(Locale.ROOT);
    }
    private SqlBaseline() {}
}
