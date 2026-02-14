注意：needSetNullAttrs不需要填写，id 不需要填写，会自动生成
# create
```location
curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/Location/create' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_token=9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D; csrf_header=X-CSRF-TOKEN; SESSION=f2cd59e4e4b3dac6edac2318bc8a5293a767a0181265f34d; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: httpƒ://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: 9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D' \
  --data-raw '{"params":{"manager":{"id":"863871307018674176","clazz":"Employee","name":""},"tenant":{"id":"-1","clazz":"Tenant","name":"basicTenant"},"needSetNullAttrs":["clsAttrs","remarks","rdmExtensionType","locationCode","parentLocation","addressText","locationName","creator","locationType","id","modifier"],"creator":"sysadmin 1","modifier":"sysadmin 1"}}' \
  --insecure
  
  curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/Location/create' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_token=9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D; csrf_header=X-CSRF-TOKEN; SESSION=f2cd59e4e4b3dac6edac2318bc8a5293a767a0181265f34d; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: 9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D' \
  --data-raw '{"params":{"locationCode":"dawf","parentLocation":{"id":"864114310232678400","clazz":"Location","name":""},"addressText":"dsad","locationName":"safasfas","manager":{"id":"863871307018674176","clazz":"Employee","name":""},"tenant":{"id":"-1","clazz":"Tenant","name":"basicTenant"},"needSetNullAttrs":["clsAttrs","remarks","rdmExtensionType","creator","locationType","id","modifier"],"creator":"sysadmin 1","modifier":"sysadmin 1"}}' \
  --insecure
```

```bom
curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/BOMItem/create' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_header=X-CSRF-TOKEN; csrf_token=F37DEC2911AC89E098A2CA3703BC734228587E77D7C857DB; SESSION=9295a4c22f6cbe0d9eba98582c9e3aa7b7247f329e2e91f0; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: F37DEC2911AC89E098A2CA3703BC734228587E77D7C857DB' \
  --data-raw '{"params":{"parentPart":{"id":"863478975819685888","clazz":"Part","name":""},"name":"dasd","source":{"id":"863478364051087360","clazz":"Part","name":""},"target":{"id":"863478975819685888","clazz":"Part","name":""},"description":"asdsa","effectiveFrom":1770739200000,"remarks":"sadaas","rdmExtensionType":"BOMItem","quantity":{"value":"1"},"findNumber":"1","uom":{"id":"863463339341127680","clazz":"Unit","name":""},"childPart":{"id":"863478364051087360","clazz":"Part","name":""},"tenant":{"id":"-1","clazz":"Tenant","name":"basicTenant"},"needSetNullAttrs":["clsAttrs","effectiveTo","modifier","creator","id"],"creator":"sysadmin 1","modifier":"sysadmin 1"}}' \
  --insecure
```

```Equipment
curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/Equipment/create' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_token=9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D; csrf_header=X-CSRF-TOKEN; SESSION=f2cd59e4e4b3dac6edac2318bc8a5293a767a0181265f34d; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: 9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D' \
  --data-raw '{"params":{"locationText":"dd","serialNumber":"asdasda","productionDate":1770048000000,"modelSpec":"333","remarks":"sda","rdmExtensionType":"Equipment","techParams":[{}],"equipmentCode":"1234","depreciationMethod":"StraightLine","serviceLifeYears":"1","status":"Standby","equipmentModelRef":{"id":"863851969981190144","clazz":"EquipmentModel","name":""},"manufacturerName":{"id":"1","clazz":"BusinessPartner","name":""},"supplierName":{"id":"1","clazz":"BusinessPartner","name":""},"brand":"哈哈哈","locationRef":{"id":"864114310232678400","clazz":"Location","name":""},"category":{"id":"863851758470828032","clazz":"EquipmentClassfication","name":""},"equipmentName":"fff","tenant":{"id":"-1","clazz":"Tenant","name":"basicTenant"},"needSetNullAttrs":["clsAttrs","modifier","creator","id"],"creator":"sysadmin 1","modifier":"sysadmin 1"}}' \
  --insecure
```

```workingProcedure
curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/WorkingProcedure/create' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_token=9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D; csrf_header=X-CSRF-TOKEN; SESSION=f2cd59e4e4b3dac6edac2318bc8a5293a767a0181265f34d; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: 9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D' \
  --data-raw '{"params":{"steps":"先吃饭后吃饭","remarks":"对对对","rdmExtensionType":"WorkingProcedure","endTime":1770652800000,"status":"InProgress","procedureName":"test666","mainProductionEquipment":{"id":"864115032261140480","clazz":"Equipment","name":""},"procedureCode":"124125","operatorRef":{"id":"863871307018674176","clazz":"Employee","name":""},"startTime":1771041934000,"operatorUser":"张三","tenant":{"id":"-1","clazz":"Tenant","name":"basicTenant"},"mainInspectionEquipment":{"id":"864115032261140480","clazz":"Equipment","name":""},"needSetNullAttrs":["clsAttrs","creator","id","modifier"],"creator":"sysadmin 1","modifier":"sysadmin 1"}}' \
  --insecure
```
```EquipmentClassfication
curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/EquipmentClassfication/create' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_header=X-CSRF-TOKEN; csrf_token=D3577AEA73D6916EF61D45A39869F4B54799FB6C2649F7AE; SESSION=273d2e676ea001274d8633ab5ef9c393daba38552c9703bb; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: D3577AEA73D6916EF61D45A39869F4B54799FB6C2649F7AE' \
  --data-raw '{"params":{"rdmExtensionType":"EquipmentClassfication","equipmentClassName":"dasdasd","tenant":{"id":"-1","clazz":"Tenant","name":"basicTenant"},"needSetNullAttrs":["clsAttrs","id","modifier","creator"],"creator":"sysadmin 1","modifier":"sysadmin 1"}}' \
  --insecure
```

```workingPlan
curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/WorkingPlan/create' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_token=9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D; csrf_header=X-CSRF-TOKEN; SESSION=f2cd59e4e4b3dac6edac2318bc8a5293a767a0181265f34d; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: 9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D' \
  --data-raw '{"params":{"productPart":{"id":"863478364051087361","clazz":"PartMaster","name":""},"branch":{"tenant":{"name":"basicTenant","id":"-1","clazz":"Tenant"},"id":"21441","needSetNullAttrs":["modifier","creator","rdmExtensionType"]},"master":{"tenant":{"name":"basicTenant","id":"-1","clazz":"Tenant"},"id":"41241","needSetNullAttrs":["modifier","rdmExtensionType","creator"]},"tenant":{"id":"-1","clazz":"Tenant","name":"basicTenant"},"securityLevel":"internal","operatorRef":{"id":"863871307018674176","clazz":"Employee","name":""},"needSetNullAttrs":["clsAttrs","branch","master","businessVersion","modifier","planDescription","kiaguid","operateTime","rdmExtensionType","workingState","creator","operatorUser","planName","name","equipmentUsage","planStatus","description","checkOutTime","preVersionId","remarks","planCode","checkOutUserName","id"],"creator":"sysadmin 1","modifier":"sysadmin 1"}}' \
  --insecure
```

```ProcedurePartLink
curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/ProcedurePartLink/create' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_token=9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D; csrf_header=X-CSRF-TOKEN; SESSION=f2cd59e4e4b3dac6edac2318bc8a5293a767a0181265f34d; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: 9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D' \
  --data-raw '{"params":{"role":"Fixture","remarks":"csad","part1":{"id":"863478364051087360","clazz":"Part","name":""},"rdmExtensionType":"ProcedurePartLink","name":"dasdas","source":{"id":"864115327103934464","clazz":"WorkingProcedure","name":""},"quantity":"123","procedure":{"id":"864115327103934464","clazz":"WorkingProcedure","name":""},"target":{"id":"863478364051087360","clazz":"Part","name":""},"uom":{"id":"863463339341127680","clazz":"Unit","name":""},"tenant":{"id":"-1","clazz":"Tenant","name":"basicTenant"},"description":"dasdsad","needSetNullAttrs":["clsAttrs","isMandatory","creator","id","modifier"],"creator":"sysadmin 1","modifier":"sysadmin 1"}}' \
  --insecure
```

```ProcedureEquipmentLink
curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/ProcedureEquipmentLink/create' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_token=9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D; csrf_header=X-CSRF-TOKEN; SESSION=f2cd59e4e4b3dac6edac2318bc8a5293a767a0181265f34d; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: 9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D' \
  --data-raw '{"params":{"role":"Auxiliary","remarks":"das","rdmExtensionType":"ProcedureEquipmentLink","name":"dsad","source":{"id":"864115327103934464","clazz":"WorkingProcedure","name":""},"actualEnd":1771776000000,"actualStart":1771948800000,"equipment1":{"id":"864115032261140480","clazz":"Equipment","name":""},"procedure":{"id":"864115327103934464","clazz":"WorkingProcedure","name":""},"target":{"id":"864115032261140480","clazz":"Equipment","name":""},"plannedStart":1771862400000,"tenant":{"id":"-1","clazz":"Tenant","name":"basicTenant"},"plannedEnd":1772121600000,"description":"asdad","needSetNullAttrs":["clsAttrs","creator","id","modifier"],"creator":"sysadmin 1","modifier":"sysadmin 1"}}' \
  --insecure
```

```PlanProcedureLink
curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/PlanProcedureLink/create' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_token=9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D; csrf_header=X-CSRF-TOKEN; SESSION=f2cd59e4e4b3dac6edac2318bc8a5293a767a0181265f34d; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: 9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D' \
  --data-raw '{"params":{"requirement":"dsada","standardDurationMin":{"value":"11"},"sequenceNo":"123","rdmExtensionType":"PlanProcedureLink","name":"dsad","source":{"id":"864115816830869504","clazz":"WorkingPlan","name":""},"procedure":{"id":"864115327103934464","clazz":"WorkingProcedure","name":""},"target":{"id":"864115327103934464","clazz":"WorkingProcedure","name":""},"plan":{"id":"864115816830869504","clazz":"WorkingPlan","name":""},"tenant":{"id":"-1","clazz":"Tenant","name":"basicTenant"},"description":"adasdasd","needSetNullAttrs":["clsAttrs","creator","id","modifier"],"creator":"sysadmin 1","modifier":"sysadmin 1"}}' \
  --insecure
```

```Part
curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/Part/create' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_header=X-CSRF-TOKEN; csrf_token=D3577AEA73D6916EF61D45A39869F4B54799FB6C2649F7AE; SESSION=273d2e676ea001274d8633ab5ef9c393daba38552c9703bb; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: D3577AEA73D6916EF61D45A39869F4B54799FB6C2649F7AE' \
  --data-raw '{"params":{"partDescription":"dasdsa","branch":{"tenant":{"name":"basicTenant","id":"-1","clazz":"Tenant"},"needSetNullAttrs":["modifier","creator","rdmExtensionType","id"]},"master":{"tenant":{"name":"basicTenant","id":"-1","clazz":"Tenant"},"needSetNullAttrs":["modifier","rdmExtensionType","creator","id"]},"category":{"id":"863477803427831808","clazz":"PartClassfication","name":""},"tenant":{"id":"-1","clazz":"Tenant","name":"basicTenant"},"securityLevel":"internal","partName":"sad","unit":{"id":"863463339341127680","clazz":"Unit","name":""},"modelSpec":"asd","supplierName":{"id":"1","clazz":"BusinessPartner","name":""},"needSetNullAttrs":["clsAttrs","branch","master","businessVersion","modifier","partNumber","extra","kiaguid","rdmExtensionType","workingState","creator","stockQty","name","drawingFile","description","checkOutTime","preVersionId","checkOutUserName","id","drawingUrl"],"creator":"sysadmin 1","modifier":"sysadmin 1"}}' \
  --insecure
```

# get
```WorkingProcedure
curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/WorkingProcedure/get' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_token=9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D; csrf_header=X-CSRF-TOKEN; SESSION=f2cd59e4e4b3dac6edac2318bc8a5293a767a0181265f34d; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: 9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D' \
  --data-raw '{"params":{"id":"864124235637858304"}}' \
  --insecure
```

```WorkingPlan
curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/WorkingPlan/get' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_token=9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D; csrf_header=X-CSRF-TOKEN; SESSION=f2cd59e4e4b3dac6edac2318bc8a5293a767a0181265f34d; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: 9176C382E19A93FBD51687DF84F48628A4426DFED00BA08D' \
  --data-raw '{"params":{"id":"864115816830869504"}}' \
  --insecure
```

```unit
{
    "result": "SUCCESS",
    "data": [
        {
            "id": "863539214308876288",
            "creator": "xdmAdmin ",
            "modifier": "xdmAdmin",
            "createTime": "2026-02-12T21:56:32.474+0800",
            "lastUpdateTime": "2026-02-12T21:56:32.474+0800",
            "rdmVersion": 1,
            "rdmDeleteFlag": 0,
            "rdmExtensionType": "Unit",
            "tenant": {
                "id": "-1",
                "creator": "xdmAdmin",
                "modifier": "xdmAdmin",
                "createTime": "2026-01-28T18:54:19.683+0800",
                "lastUpdateTime": "2026-01-28T18:54:19.683+0800",
                "rdmVersion": 1,
                "rdmDeleteFlag": 0,
                "rdmExtensionType": "Tenant",
                "tenant": null,
                "className": "Tenant",
                "name": "basicTenant",
                "description": "默认租户",
                "kiaguid": null,
                "securityLevel": "internal",
                "nameEn": null,
                "code": "basicTenant",
                "disableFlag": false,
                "dataSource": "DefaultDataSource"
            },
            "className": "Unit",
            "unitName": "测试单位",
            "mesurementSystem": null,
            "unitFactor": null,
            "unitDisplayName": null,
            "unitCategory": null
        }
    ],
    "errors": []
}
```



# update
```javascript
curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/WorkingProcedure/update' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_header=X-CSRF-TOKEN; csrf_token=D3577AEA73D6916EF61D45A39869F4B54799FB6C2649F7AE; SESSION=273d2e676ea001274d8633ab5ef9c393daba38552c9703bb; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: D3577AEA73D6916EF61D45A39869F4B54799FB6C2649F7AE' \
  --data-raw '{"params":{"rdmExtensionType":"WorkingProcedure","status":"InProgress","creator":"sysadmin 1","mainProductionEquipment":{"id":"864115032261140480","clazz":"Equipment","name":""},"operatorRef":{"id":"863871307018674176","clazz":"Employee","name":""},"startTime":1771948800000,"id":"864124235637858304","modifier":"sysadmin 1","tenant":{"id":"-1","clazz":"Tenant","name":"basicTenant"},"mainInspectionEquipment":{"id":"864115032261140480","clazz":"Equipment","name":""},"needSetNullAttrs":["clsAttrs","steps","remarks","endTime","procedureName","procedureCode","operatorUser"]}}' \
  --insecure
```

```bom
curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/BOMItem/update' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_header=X-CSRF-TOKEN; csrf_token=F37DEC2911AC89E098A2CA3703BC734228587E77D7C857DB; SESSION=9295a4c22f6cbe0d9eba98582c9e3aa7b7247f329e2e91f0; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: F37DEC2911AC89E098A2CA3703BC734228587E77D7C857DB' \
  --data-raw '{"params":{"parentPart":{"id":"863478975819685888","clazz":"Part","name":""},"name":"dasd","source":{"id":"863478364051087360","clazz":"Part","name":""},"effectiveTo":1771776000000,"target":{"id":"863478975819685888","clazz":"Part","name":""},"modifier":"sysadmin 1","description":"asdsa","effectiveFrom":1770739200000,"remarks":"sadaas","rdmExtensionType":"BOMItem","quantity":{"value":"1.000000"},"findNumber":"1","creator":"sysadmin 1","uom":{"id":"863463339341127680","clazz":"Unit","name":""},"id":"864214404529397760","childPart":{"id":"863478364051087360","clazz":"Part","name":""},"tenant":{"id":"-1","clazz":"Tenant","name":"basicTenant"},"needSetNullAttrs":["clsAttrs"]}}' \
  --insecure
```

# 特殊：版本号
```workingplan
curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/WorkingPlan/checkout' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_header=X-CSRF-TOKEN; csrf_token=D3577AEA73D6916EF61D45A39869F4B54799FB6C2649F7AE; SESSION=273d2e676ea001274d8633ab5ef9c393daba38552c9703bb; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: D3577AEA73D6916EF61D45A39869F4B54799FB6C2649F7AE' \
  --data-raw '{"params":{"masterId":"41241","modifier":"sysadmin 1","workCopyType":"BOTH"}}' \
  --insecure
  
  curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/WorkingPlan/update' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_header=X-CSRF-TOKEN; csrf_token=D3577AEA73D6916EF61D45A39869F4B54799FB6C2649F7AE; SESSION=273d2e676ea001274d8633ab5ef9c393daba38552c9703bb; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: D3577AEA73D6916EF61D45A39869F4B54799FB6C2649F7AE' \
  --data-raw '{"params":{"productPart":{"id":"863478364051087361","clazz":"PartMaster","name":""},"branch":{"tenant":{"name":"basicTenant","id":"-1","clazz":"Tenant"},"id":"21441","clazz":"WorkingPlanBranch","name":"","modifier":"sysadmin 1","creator":"sysadmin 1","rdmExtensionType":"WorkingPlanBranch"},"master":{"tenant":{"name":"basicTenant","id":"-1","clazz":"Tenant"},"modifier":"sysadmin 1","rdmExtensionType":"WorkingPlanMaster","creator":"sysadmin 1","id":"41241","clazz":"WorkingPlanMaster","name":""},"modifier":"sysadmin 1","planDescription":"dsa","rdmExtensionType":"WorkingPlan","workingState":"INWORK","creator":"sysadmin 1","planName":"das","tenant":{"id":"-1","clazz":"Tenant","name":"basicTenant"},"securityLevel":"internal","description":"dsadas","checkOutTime":1771054427240,"preVersionId":"864115816830869504","remarks":"ads","checkOutUserName":"sysadmin 1","operatorRef":{"id":"863871307018674176","clazz":"Employee","name":""},"id":"864167666875637760","needSetNullAttrs":["clsAttrs","businessVersion","kiaguid","operateTime","operatorUser","name","equipmentUsage","planStatus","planCode"]}}' \
  --insecure
  
  curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/WorkingPlan/checkin' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_header=X-CSRF-TOKEN; csrf_token=D3577AEA73D6916EF61D45A39869F4B54799FB6C2649F7AE; SESSION=273d2e676ea001274d8633ab5ef9c393daba38552c9703bb; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: D3577AEA73D6916EF61D45A39869F4B54799FB6C2649F7AE' \
  --data-raw '{"params":{"viewNo":"","masterId":"41241","modifier":"sysadmin 1"}}' \
  --insecure
```
```part version
curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/Part/checkout' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_header=X-CSRF-TOKEN; csrf_token=A070487F07E1DEED3E7AFA347DE16C50AB7EE20432D46FBF; SESSION=9f8261a38a97d4f161aeb9af0850d2668598b24b9e09b8da; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: A070487F07E1DEED3E7AFA347DE16C50AB7EE20432D46FBF' \
  --data-raw '{"params":{"masterId":"864205130587709441","modifier":"sysadmin 1","workCopyType":"BOTH"}}' \
  --insecure
  
  curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/Part/update' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_header=X-CSRF-TOKEN; csrf_token=A070487F07E1DEED3E7AFA347DE16C50AB7EE20432D46FBF; SESSION=9f8261a38a97d4f161aeb9af0850d2668598b24b9e09b8da; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: A070487F07E1DEED3E7AFA347DE16C50AB7EE20432D46FBF' \
  --data-raw '{"params":{"branch":{"tenant":{"name":"basicTenant","id":"-1","clazz":"Tenant"},"id":"864205130587709442","clazz":"PartBranch","name":"","modifier":"sysadmin 1","creator":"sysadmin 1","rdmExtensionType":"PartBranch"},"master":{"tenant":{"name":"basicTenant","id":"-1","clazz":"Tenant"},"modifier":"sysadmin 1","rdmExtensionType":"PartMaster","creator":"sysadmin 1","id":"864205130587709441","clazz":"PartMaster","name":""},"modifier":"sysadmin 1","rdmExtensionType":"Part","workingState":"INWORK","creator":"sysadmin 1","category":{"id":"863477803427831808","clazz":"PartClassfication","name":""},"tenant":{"id":"-1","clazz":"Tenant","name":"basicTenant"},"securityLevel":"internal","description":"ad","checkOutTime":1771063373965,"unit":{"id":"863463339341127680","clazz":"Unit","name":""},"preVersionId":"864205160023334912","supplierName":{"id":"1","clazz":"BusinessPartner","name":""},"checkOutUserName":"sysadmin 1","id":"864205192160092160","needSetNullAttrs":["clsAttrs","partDescription","businessVersion","partNumber","extra","kiaguid","stockQty","name","drawingFile","partName","modelSpec","drawingUrl"]}}' \
  --insecure
  
  curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/Part/checkin' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_header=X-CSRF-TOKEN; csrf_token=A070487F07E1DEED3E7AFA347DE16C50AB7EE20432D46FBF; SESSION=9f8261a38a97d4f161aeb9af0850d2668598b24b9e09b8da; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: A070487F07E1DEED3E7AFA347DE16C50AB7EE20432D46FBF' \
  --data-raw '{"params":{"viewNo":"","masterId":"864205130587709441","modifier":"sysadmin 1"}}' \
  --insecure
  
```

```part
curl 'http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/dynamic/api/Part/get' \
  -H 'Accept: application/json, text/plain, */*' \
  -H 'Accept-Language: zh-CN' \
  -H 'Accept-TenseContext: runtime' \
  -H 'Cache-Control: no-cache' \
  -H 'Content-Type: application/json' \
  -b 'csrf_header=X-CSRF-TOKEN; csrf_token=D3577AEA73D6916EF61D45A39869F4B54799FB6C2649F7AE; SESSION=273d2e676ea001274d8633ab5ef9c393daba38552c9703bb; XSRF-TOKEN=C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'Origin: http://99.suyiiyii.top:8003' \
  -H 'Pragma: no-cache' \
  -H 'Proxy-Connection: keep-alive' \
  -H 'Referer: http://99.suyiiyii.top:8003/rdm_a1b52ff379ee46ed8928d7f2ceb908f6_app/services/index.html' \
  -H 'User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/144.0.0.0 Safari/537.36' \
  -H 'X-Dme-Timezone: UTC+08:00' \
  -H 'X-Requested-With: XMLHttpRequest' \
  -H 'X-User-Id: 1' \
  -H 'X-XSRF-TOKEN: C7F07B189CB6CF79FF0E07451A974E8CBAB1861ABAD50A9D' \
  -H 'applicationId: a1b52ff379ee46ed8928d7f2ceb908f6' \
  -H 'modifier: sysadmin 1' \
  -H 'tenantId: -1' \
  -H 'x-csrf-token: D3577AEA73D6916EF61D45A39869F4B54799FB6C2649F7AE' \
  --data-raw '{"params":{"id":"864177125324759040"}}' \
  --insecure
```