```java
private String getSomething(String url, String jsonName) {
	BufferedReader reader = null;
	HttpURLConnection connection = null;
	try {
		URL getUrl = new URL(url);
		connection = (HttpURLConnection) getUrl.openConnection();
		connection.setConnectTimeout(2000);
        connection.setReadTimeout(5000);
		connection.connect();
		reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String lines;
		String result = "";
          while ((lines = reader.readLine()) != null) {
          	result = result + lines;
          }
          JSONObject jsonObject = JSONObject.parseObject(result);
          return String.valueOf(jsonObject.get(jsonName));
	} catch (MalformedURLException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		 try {
			reader.close();
			connection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	return null;
}
```  

```java
/**
 * 定时更新摄影师评论星级
 */
@Transactional
public void updateProCmtStar() {
    List<PsPro> pros = this.psProService.findAllUndeletedRecords();
    logger.info("更新摄影师评价星级开始，查询出摄影师总数：" + pros.size());
    //查询出所有的摄影师带星级评价数据
    List<AtEvaluation> evaluations = this.atEvaluationService.listAllProEvaluations()
            .parallelStream()
            .filter(evaluation -> (evaluation.getServiceAtti() != null  && evaluation.getPhotoEffect() != null && evaluation.getBackSpeed() != null))
            .collect(Collectors.toList());
    logger.info("更新摄影师评价星级，查询出摄影师评价总数：" + evaluations.size());
    //计算所有摄影师的服务态度平均值
    Map<Integer, Double> proServiceAttis = evaluations.parallelStream()
            .collect(Collectors.groupingBy(AtEvaluation::getBeCmterId, Collectors.averagingDouble(AtEvaluation::getServiceAtti)));
    //计算所有摄影师的拍摄效果平均值
    Map<Integer, Double> proPhotoEffects = evaluations.parallelStream()
            .collect(Collectors.groupingBy(AtEvaluation::getBeCmterId, Collectors.averagingDouble(AtEvaluation::getPhotoEffect)));
    //计算所有摄影师的交片速度平均值
    Map<Integer, Double> proBackSpeeds = evaluations.parallelStream()
            .collect(Collectors.groupingBy(AtEvaluation::getBeCmterId, Collectors.averagingDouble(AtEvaluation::getBackSpeed)));

    //更新摄影师数组中评价星级有变动的摄影师
    pros = pros.parallelStream().filter(pro ->
            (!(proServiceAttis.get(pro.getUsrId()) == null ? new Double(0d) : proServiceAttis.get(pro.getUsrId())).equals(pro.getServiceAtti())
                    || !(proPhotoEffects.get(pro.getUsrId()) == null ? new Double(0d) : proPhotoEffects.get(pro.getUsrId())).equals(pro.getPhotoEffect())
                    || !(proBackSpeeds.get(pro.getUsrId()) == null ? new Double(0d) : proBackSpeeds.get(pro.getUsrId())).equals(pro.getBackSpeed()))
    ).collect(Collectors.toList());
    logger.info("更新摄影师评价星级，需要更新的摄影师数据总数：" + pros.size());
    pros.parallelStream().forEach(pro -> {
        pro.setServiceAtti(proServiceAttis.get(pro.getUsrId()) == null ? 0d : proServiceAttis.get(pro.getUsrId()));
        pro.setPhotoEffect(proPhotoEffects.get(pro.getUsrId()) == null ? 0d : proPhotoEffects.get(pro.getUsrId()));
        pro.setBackSpeed(proBackSpeeds.get(pro.getUsrId()) == null ? 0d : proBackSpeeds.get(pro.getUsrId()));
    });

    this.psProService.updateAll(pros);
}
```  
