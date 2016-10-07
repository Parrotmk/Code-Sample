<?php
$installer = $this;
$installer->startSetup();
$sql=<<<SQLTEXT
CREATE TABLE IF NOT EXISTS `enquiry` (
  `name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `telephone` int(20) NOT NULL,
  `comment` varchar(1000) NOT NULL,
  `reply_message` varchar(1000) NOT NULL,
  `created_date` datetime NOT NULL,
  `updated_date` datetime NOT NULL,
  `enquiry_id` int(255) NOT NULL AUTO_INCREMENT,
  `created_By` varchar(255) NOT NULL,
  `updated_By` varchar(255) NOT NULL,
  `status_id` tinyint(100) NOT NULL,
  PRIMARY KEY (`enquiry_id`)
) 
		
SQLTEXT;
$installer->run($sql);
$installer->endSetup();
	 