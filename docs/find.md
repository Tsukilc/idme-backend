Find
更新时间：2025-10-22 GMT+08:00
查看PDF
分享
功能介绍
根据指定条件查询，返回数据模型的所有属性及关联的信息。

入参
POST http://{Endpoint}/rdm_{appID}_app/services/dynamic/api/{entityName}/find/pageSize/curPage
{
"params": {
"sorts": [
{
"sort": "DESC",
"orderBy": "属性名称"
}

        ],
        "filter": {
           ......
        },
        "isNeedTotal": true
    }
}

{Endpoint}表示数据建模引擎所在域名或IP地址，{appID}表示应用ID，{entityName}表示实体的英文名称。
在URL上填写待查询的页码（curPage）和每页可显示的数据量（pageSize）。
在JSON代码中设置sorts字段和filter字段。
sorts：填写需要按哪个字段进行排序，可填写模型自身属性、参考对象的属性、扩展属性及分类属性，也可为空。
filter：填写过滤条件，可为空。
出参
返回模型所有属性、直接关联的参考对象、扩展属性、分类属性、级联的数据等。

示例场景
有一个实例（People），先按名称倒序排序，再按年龄倒序排序。而后根据性别为男性进行过滤。

入参示例
POST http://dme.cn-north-4.huaweicloud.com/rdm_01a2b2c4764d4e00f123g345fd9baa9f_app/services/dynamic/api/People/find/20/1
{
"params": {
"sorts": [
{
"sort": "DESC",
"orderBy": "name"
},
{
"sort": "DESC",
"orderBy": "age"
}
],
"filter": {
"joiner": "and",
"conditions": [
{
"conditionName": "gender",
"operator": "=",
"conditionValues": [
"男"
]
}
]
},
"isNeedTotal": true
}
}

出参示例
{
"result": "SUCCESS",
"data": [
{
"id": "455304645330341888",
"creator": "test1 3c03e719256a427eb9277b64fcXXXXXX",
"createTime": "2023-01-13T01:38:07.000+00:00",
"modifier": "test1 3c03e719256a427eb9277b64fcXXXXXX",
"lastUpdateTime": "2023-01-13T01:38:07.000+00:00",
"rdmVersion": 1,
"rdmExtensionType": "People",
"rdmDeleteFlag": 0,
"tenant": {
"id": "-1",
"clazz": "Tenant"
},
"className": "People",
"name": "李四",
"description": null,
"kiaguid": null,
"securityLevel": "internal",
"gender": "男",
"age": 20
},
{
"id": "455304534248394752",
"creator": "test1 3c03e719256a427eb9277b64fcXXXXXX",
"createTime": "2023-01-13T01:37:40.000+00:00",
"modifier": "test1 3c03e719256a427eb9277b64fcXXXXXX",
"lastUpdateTime": "2023-01-13T01:37:40.000+00:00",
"rdmVersion": 1,
"rdmExtensionType": "People",
"rdmDeleteFlag": 0,
"tenant": {
"id": "-1",
"clazz": "Tenant"
},
"className": "People",
"name": "张三",
"description": null,
"kiaguid": null,
"securityLevel": "internal",
"gender": "男",
"age": 18
}
],
"errors": [],
"pageInfo": {
"curPage": 1,
"pageSize": 20,
"totalRows": 2,
"totalPages": 1
}
}