Aerial
======

**Aerial** is an engine implementing the approach of **Executable Requirements**. Mainly it is designed as an extension of **Cucumber** to provide the following possibilities:
* **More compact and structured representation of requirements and scenarios** - actually text instructions are targeted to look more like requirements or technical specifications rather than scenarios
* **Built-in mechanism for generating test scenarios** - main idea is that test scenarios are generated based on formal description of some feature and it's attributes. This minimises efforts on test data definition and data preparation. You just have to specify what is the data and how each data item is relevant to each other
* **Generalised approach for getting data from external resources** - requirements can be stored in any form and in any place (or system). The **Aerial** should provide extensible mechanism to retrieve requirements from various different data sources 
* **Ability to perform static checks on requirements** - since we expand requirements into tests using some rules there's an ability to find out requirement inconsistencies during initial processing stage
* **Tests and their automated implementation reacts on any requirement change** - most of test management systems simply store dedicated records for requirements, tests and automated tests. But they are just linked by abstract DB structure. Thus, if we do any modification into one of those items others wouldn't be reflected. Since **Aerial** generates tests and automated tests based on requirements as an input, any change to requirements will be immediately reflected. So, tests always correspond to requirements.
* **Simplify requirements and test coverage calculation** - mainly such coverage is 100% by design

How does it works? 
======

Typical document structure
======

Features
======

User Reference Guide
======

Releases
======

Documentation
======

Blog Posts & Live Demos
======

[Blog: Test Automation From Inside](http://mkolisnyk.blogspot.com)

Authors
======
Myk Kolisnyk (kolesnik.nickolay@gmail.com) 

<a href="http://ua.linkedin.com/pub/mykola-kolisnyk/14/533/903"><img src="http://www.linkedin.com/img/webpromo/btn_profile_bluetxt_80x15.png" width="80" height="15" border="0" alt="View Mykola Kolisnyk's profile on LinkedIn"></a>
<a href="http://plus.google.com/108480514086204589709?prsrc=3" rel="publisher" style="text-decoration:none;">
<img src="http://ssl.gstatic.com/images/icons/gplus-16.png" alt="Google+" style="border:0;width:16px;height:16px;"/></a>
