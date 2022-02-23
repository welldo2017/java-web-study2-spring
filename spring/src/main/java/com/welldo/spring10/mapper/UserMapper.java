package com.welldo.spring10.mapper;

import java.util.List;

import com.welldo.spring10.bean.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;


/**
 * 1.MyBatis使用Mapper来实现映射，而且Mapper必须是接口。
 * 我们以User类为例，在User类和users表之间映射的UserMapper编写如下：
 *
 * 2.注意：这里的Mapper不是JdbcTemplate的RowMapper的概念，
 * 它是定义访问users表的接口方法。
 * 比如我们定义了一个User getById(long)的主键查询方法，不仅要定义接口方法本身，还要明确写出查询的SQL，
 * 这里用注解@Select标记。
 *
 * SQL语句的任何参数，都与方法参数按名称对应。
 * 例如，方法参数id的名字通过注解@Param()标记为id，则SQL语句里将来替换的占位符就是#{id}。
 *
 *
 * 3.有了UserMapper接口，还需要对应的实现类才能真正执行这些数据库操作的方法
 *
 * 虽然可以自己写实现类，但我们除了编写UserMapper接口外，还有BookMapper、BonusMapper……一个一个写太麻烦，
 * 因此，MyBatis提供了一个MapperFactoryBean,来自动创建所有Mapper的实现类。
 * 可以用一个简单的注解来启用它： * @MapperScan("com.xxx.xxx.mapper")(写在config类上面)
 *
 * 在真正的业务逻辑中，我们可以直接注入：
 *     @Autowired
 *     UserMapper userMapper;
 *
 *
 * 4.可见，业务逻辑主要就是通过XxxMapper定义的数据库方法来访问数据库。
 */
public interface UserMapper {

	/**
	 * 如果列名和属性名不同，需要编写别名：
	 * -- 列名是created_time，属性名是createdAt:
	 * SELECT id, name, email, created_time AS createdAt FROM users
	 */
	@Select("SELECT * FROM users WHERE id = #{id}")
	User getById(@Param("id") long id);


	@Select("SELECT * FROM users WHERE email = #{email}")
	User getByEmail(@Param("email") String email);


	@Select("SELECT * FROM users LIMIT #{offset}, #{maxResults}")
	List<User> getAll(@Param("offset") int offset, @Param("maxResults") int maxResults);


	/**
	 * 执行INSERT语句就稍微麻烦点，因为我们希望传入User实例，在SQL中引用的时候，以#{obj.property}的方式写占位符
	 * 如果users表的id是自增主键，那么，我们在SQL中不传入id，
	 * 但希望获取插入后的主键，需要再加一个@Options注解：
	 */
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	@Insert("INSERT INTO users (email, password, name, createdAt) " +
			"VALUES (#{user.email}, #{user.password}, #{user.name}, #{user.createdAt})")
	void insert(@Param("user") User user);



	//相对简单
	@Update("UPDATE users SET name = #{user.name}, createdAt = #{user.createdAt} WHERE id = #{user.id}")
	void update(@Param("user") User user);


	//相对简单
	@Delete("DELETE FROM users WHERE id = #{id}")
	void deleteById(@Param("id") long id);
}
