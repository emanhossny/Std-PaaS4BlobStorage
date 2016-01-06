<html>
 <head>
  <title> The Std-PaaS API Web client for the Cloud Storage Service</title>
  <script type='text/javascript' src='http://code.jquery.com/jquery-1.7.1.js'></script>
<!-- Code Mirror -->
<link rel="stylesheet" href="ressources/codeMirror/lib/codemirror.css">
    <script src="ressources/codeMirror/lib/codemirror.js"></script>
    <script src="ressources/codeMirror/mode/xml/xml.js"></script>
    <style type="text/css">.foo{border-right: 1px solid red} .CodeMirror {border-top: 1px solid black; border-bottom: 1px solid black;}</style>
    <link rel="stylesheet" href="ressources/codeMirror/doc/docs.css">
    <link rel="stylesheet" href="ressources/codeMirror/theme/rubyblue.css">
    <link rel="stylesheet" href="ressources/codeMirror/theme/eclipse.css">
    <script src="ressources/codeMirror/lib/util/formatting.js"></script>

<!-- for the table -->
<style type="text/css">
table {border: 1px solid;overflow:hidden; width:whatever its width is in pixels, percent, or em; margin: 0 auto;}
</style>

 </head>
 <body bgcolor="#3E7087">

<div style="text-align: center;"> </div>
 <div align="center"><h2><font color="white">Std-PaaS API Web client<br> for the Cloud Storage Service</font></h2><h4> </h4> </div>
<form method="post" action="/Std-Client/StdPersistentStorageClient" style="height: 959px; ">
 <div align="center">
	<table border="0" bordercolor="white" bgcolor="#1F4661" width="60%" cellpadding="3" cellspacing="3" style="height: 148px; ">
	<tr>
		<td><font color="white">Cloud Storage <br>Descriptor: </font></td>
		<td></td>
		<td><div style="width: 584px; border:solid 1px black;">
		<textarea style="background:white" id="storage_manifest" name="storage_manifest" rows="5" cols="70"></textarea></div></td>
	</tr>
	
	<tr><td><font color="white">Select File to upload:</font></td><td>&nbsp;</td> 
	<td><input type="file" name="file" size="45" style="width: 590px; "/></td>
	</tr>

		<tr>
		<td></td>
		<td></td>
		<td align=right style="height: 52px; "><input type="submit" value="Submit"></td>
	</tr>
	</table>
	</div>
  </form>
   <script>
        var editor2 = CodeMirror.fromTextArea(document.getElementById("storage_manifest"), {
        mode: {name: "xml", alignCDATA: true},
        lineNumbers: false, theme: "rubyblue", indentWithTabs: true, indentUnit: 4
  
      });
      
    </script>	
  		
 </body>
</html>
