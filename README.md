# pxeserver
Implementation of DHCP proxy supporting PXE boot.

<<<<<<< HEAD
<<<<<<< HEAD
This is not a DHCP server. You must have a separate DHCP server (such as your home router) running concurrently to this proxy server. 
For more details, please refer to the user guide inside the docs folder.

Follows Intel Specification version 2.1 for PXE boot process. Supports iPXE and gPXE clients. Supports clients whose system architecture is a nonzero value.

Note that the client gets the files themselves from a tftp server. This project does not provide a tftp server, 
you as the user must find and configure one independently. This project, the proxy DHCP server, serves to 
redirect the client to the tftp server with the correct filename.

This was created because dnsmasq is not available on windows and isc-dhcp is not usable as a pure proxy server.
Additionally, this is easier to configure than the aforementioned, although it is more limited. Since this is a 
java project it is also platform independent.
=======
This is not a DHCP server. You must have a separate DHCP server (such as your home router)
running concurrently to this proxy server. For more details, please refer to the user guide
inside the docs folder.
=======
This is not a DHCP server. You must have a separate DHCP server (such as your home router) running concurrently to this proxy server. 
For more details, please refer to the user guide inside the docs folder.
>>>>>>> 8a216b0746938eec9d8f060566b4ca1ab44b7273

Follows Intel Specification version 2.1 for PXE boot process. Supports iPXE and gPXE clients. Supports clients whose system architecture is a nonzero value.

<<<<<<< HEAD
Note that the client gets the files themselves from a tftp server. This project does not provide a tftp 
server, you as the user must find and configure one independently. This project, the proxy DHCP server,
serves to redirect the client to the tftp server with the correct filename. 
>>>>>>> 1129494caeb1c5e86b797d5cc9870422ada7b099
=======
Note that the client gets the files themselves from a tftp server. This project does not provide a tftp server, 
you as the user must find and configure one independently. This project, the proxy DHCP server, serves to 
redirect the client to the tftp server with the correct filename.

This was created because dnsmasq is not available on windows and isc-dhcp is not usable as a pure proxy server.
Additionally, this is easier to configure than the aforementioned, although it is more limited. Since this is a 
java project it is also platform independent.
>>>>>>> 8a216b0746938eec9d8f060566b4ca1ab44b7273
