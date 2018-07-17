
import java.util.List;
import java.util.Map;

/**
 * @author wpl
 */
public interface SqlConvert {

    /**
     * @param sqlContext sql语句集合
     * @return sql语句分类对应的集合
     * @throws Exception
     */
    Map<String,List<String>> transfromSqlClassify(String sqlContext) throws Exception;
}
