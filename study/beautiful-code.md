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
