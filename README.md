1.Dbhelper is a tool which can help user to export the data from text or oracle db and import into the mysql
2.Usage
  Dbhelper are using json file to define the transfer rule.for example,if you want to import text file
  into the mysql,you can defined the config file as below:
  {
  	"type": "TXT2MYSQL",
  	"name": "user",
  	"datasource": {
  		"dataFilePath": "D:\\user",
  		"seperator": ","
  	},
  	"mapping": {
  		"name": "user",
  		"metaList": [{
  				"colNumber": 0,
  				"colName": "name",
  				"type": "varchar",
  				"length": 50,
  				"comment": "用户名"
  			},
  			{
  				"colNumber": 1,
  				"colName": "age",
  				"type": "int",
  				"comment": "年龄"
  			},
  			{
  				"colNumber": 2,
  				"colName": "sex",
  				"type": "varchar",
  				"length": 10,
  				"comment": "性别"
  			},
  			{
  				"colNumber": 3,
  				"colName": "department",
  				"type": "varchar",
  				"length": 100,
  				"comment": "部门"
  			}
  		]
  	},
  	"target": {
  		"url": "jdbc:mysql://127.0.0.1:3306/dbhelper?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8",
  		"userName": "root",
  		"password": "12345678"
  	}
  }

