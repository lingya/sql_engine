
import com.meitu.mlink.sqlengine.api.sql.SqlConvert;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.meitu.mlink.sqlengine.api.sql.SqlConstant.*;

/**
 * @author wpl
 */
public class SqlConvertImpl implements SqlConvert {
    private static final Logger logger = LoggerFactory.getLogger(SqlConvertImpl.class);

    public SqlConvertImpl() {

    }

    @Override
    public Map<String,List<String>> transfromSqlClassify(String sqlContext) throws Exception {
        if (StringUtils.isBlank(sqlContext)){
            logger.warn("sql is null");
            throw new Exception("sql not null");
        }

        logger.warn("user input sql is : "+sqlContext);

        List<String> sqls = splitSemiColon(sqlContext);
        if (CollectionUtils.isEmpty(sqls)){
            logger.warn("sqls is null");
            throw new Exception("sqls not null");
        }

        Map<String,List<String>> result = new HashMap<>();
        List<String> commList = new ArrayList<>();
        List<String> sourceList = new ArrayList<>();
        List<String> sinkList = new ArrayList<>();

        for (String sql: sqls) {
            if (StringUtils.isNotBlank(sql)){
                //替换多余的空格
                String newSql = sql.trim().replaceAll(" +", " ").replaceAll("\\s+", " ");

                if (newSql.toUpperCase().startsWith(CREATE)){
                    if (newSql.toUpperCase().contains(TABLESOURCE)){
                        sourceList.add(newSql+SQL_END_FLAG);
                    }

                    if (newSql.toUpperCase().contains(TABLESINK)){
                        sinkList.add(newSql+SQL_END_FLAG);
                    }

                    if (newSql.toUpperCase().contains(FUNCTION)){
                        commList.add(newSql+SQL_END_FLAG);
                    }
                }else {
                    commList.add(newSql+SQL_END_FLAG);
                }

            }
        }

        if (CollectionUtils.isNotEmpty(commList)){
            result.put(COMM_AND_QUERY,commList);
        }
        if (CollectionUtils.isNotEmpty(sourceList)){
            result.put(TABLESOURCE,sourceList);
        }
        if (CollectionUtils.isNotEmpty(sinkList)){
            result.put(TABLESINK,sinkList);
        }

        return result;
    }

    private  List<String> splitSemiColon(String sqlContext) {
        boolean inQuotes = false;
        boolean escape = false;

        List<String> ret = new ArrayList<>();

        char quoteChar = '"';
        int beginIndex = 0;
        for (int index = 0; index < sqlContext.length(); index++) {
            char c = sqlContext.charAt(index);
            switch (c) {
                case ';':
                    if (!inQuotes) {
                        ret.add(sqlContext.substring(beginIndex, index));
                        beginIndex = index + 1;
                    }
                    break;
                case '"':
                case '\'':
                    if (!escape) {
                        if (!inQuotes) {
                            quoteChar = c;
                            inQuotes = !inQuotes;
                        } else {
                            if (c == quoteChar) {
                                inQuotes = !inQuotes;
                            }
                        }
                    }
                    break;
                default:
                    break;
            }

            if (escape) {
                escape = false;
            } else if (c == '\\') {
                escape = true;
            }
        }

        if (beginIndex < sqlContext.length()) {
            ret.add(sqlContext.substring(beginIndex));
        }

        return ret;
    }
}