<?php 
global $wpdb;
$id = $_GET['id'];
//echo $id;
if(isset($_POST['submit']))
{
	
	 global $wpdb;
	 $homeUrl = home_url();
	 $ids= $_GET['id'];
	 $to = $email = $_POST['email'];
	 $name = $_POST['name'];
	 $content = stripslashes($_POST['replycomment']);
	 $subject = "ShripalSingh.com | Reply of contact";
	 $message = "
			<html lang='en'>
			<head>
			<meta charset='utf-8'>
            <meta http-equiv='X-UA-Compatible' content='IE=edge'>
            <meta name='viewport' content='width=device-width, initial-scale=1'>
           <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
           <title></title>

          <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
          <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
          <!--[if lt IE 9]>
          <script src='http://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js'></script>
          <script src='http://oss.maxcdn.com/respond/1.4.2/respond.min.js'></script>
          <![endif]-->
			</head>
			<body>
			<table width='600px' cellpadding='0' cellspacing='0' border='0' style='margin:0 auto; width:600px; max-width:600px; font-family:Arial, Helvetica, sans-serif; font-size:13px; line-height:20px; '>
		  <tr>
			<td style='background-color:#fafafa;'><img src='$homeUrl/wp-content/themes/shripal/img/shri.jpg'/></td>
		  </tr>
		  <tr>
			<td style='padding:25px 15px; background:#fafafa'>
				<h1 style='font-size:16px; color:#000000; margin:0 0 5px 0;'>Dear  $name,</h1>
				<br/>
				<p style='color:#555555; font-size:13px; margin:0px;'> 
				$content
				<br/><br/>
				Thank you.
				</p>
			</td>
		  </tr>
		  <tr>
			<td style='background:#e4e4e4; padding:8px 0px; text-align:center;'>
				<span style='font-size:12px; color:#555555;'>Copyright &copy; 2016 <a style='color:#555' href='http://shripalsingh.com'>shripalsingh.com</a>. All Rights Reserved.</span>
			</td>
		  </tr>
		</table>
			
		</body>
			</html>
			";
	          
			    $headers = "MIME-Version: 1.0" . "\r\n";
			$headers .= "Content-type:text/html;charset=UTF-8" . "\r\n";

			// More headers
			$headers .= 'From: Shripal Singh<admin@shripalsingh.com>' . "\r\n";
			
			$headers .= "Reply-To:noreply@shripalsingh.com"."\r\n"; 
		  $headers .= "Organization: Shripal Singh Blog \r\n";
		  $headers .= "X-Priority: 3\r\n";
		  $headers .= "X-Mailer: PHP". phpversion() ."\r\n" ;
		  
		  
		  
		  
			$mail = mail($to,$subject,$message,$headers);



















$message = "Email sent successfully";





	

//mail($to,$subject,$content,$headers);
   
 } 
$contact = $wpdb->get_results("SELECT * FROM wp_contactus WHERE id='$id' ");?>
















<h1>Contact Details:</h1>
<H3 style="color:green"><?php echo @$message ;?></h3>
<form action="" method="post" id="guestdetail">
<table width="50%" border="1"  class="wp-list-table widefat fixed striped posts">
 <tr>
  <th width="20%">Name:&nbsp;</th>
  <td><input type="hidden" name="name" value="<?php echo $contact[0]->contact_firstname." ".$contact[0]->contact_lastname;;?>"><?php echo $contact[0]->contact_firstname." ".$contact[0]->contact_lastname;?></td>
 </tr>
 <tr>
  <th>Email:&nbsp;</th>
  <td><?php echo $contact[0]->contact_email;?></td>
  <input type="hidden" name="email" value="<?php echo $contact[0]->contact_email;?>">
 </tr>
 <tr>
  <th>Phone no:&nbsp;</th>
  <td><?php echo $contact[0]->contact_ph;?></td>
 </tr>
 <tr>
  <th>Comments:&nbsp;</th>
  <td><?php echo stripslashes($contact[0]->contact_message);?></td>
 </tr>
 <tr>
  <th>Created At:&nbsp;</th>
   <?php $date = strtotime($contact[0]->contact_created	);?>
  <td><?php echo date("j F Y", $date);?></td>
 </tr>
 <tr>
  <th>Reply Comments:&nbsp;</th>
  <td><?php wp_editor(
    $contact[0]->null,
    'replycomment',
    array( 'textarea_name' => 'replycomment' ,'rows' => 4 ,'cols' => 50)
    );?>
</td>
 </tr>
 
</table> 
<input type="submit" name="submit" value="Reply" style="color:white;background-color:green;"> 
</form>

