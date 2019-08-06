import com.xkx.config.oracle.OracleDataSource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OracleDataSourceTest {


    private OracleDataSource oracleDataSource;

    @Before
    public void setUp() {
        this.oracleDataSource = new OracleDataSource();
        this.oracleDataSource.setTableName("M_AREA");
    }

    @Test
    public void testCreateSelectSqlWhenPageIsTrue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        int rowCount = 50;
        Method method = this.oracleDataSource.getClass().getDeclaredMethod("createSelectSql", boolean.class);
        method.setAccessible(true);
        while(this.oracleDataSource.getPosition()<=rowCount){
            String expectSql = "select * from ( select a.*,rownum rn from (select * from M_AREA) a) where rn>="+this.oracleDataSource.getPosition()+ " and rn<"+(this.oracleDataSource.getPosition()+this.oracleDataSource.getSize());
            String sql  = (String) method.invoke(this.oracleDataSource,true);
            System.out.println(sql);
            Assert.assertEquals(sql,expectSql);
            this.oracleDataSource.setPosition(this.oracleDataSource.getPosition()+this.oracleDataSource.getSize());
        }
    }


}
