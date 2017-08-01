# pxeserver
Implementation of DHCP proxy supporting PXE boot.

This is not a DHCP server. You must have a separate DHCP server (such as your home router)
running concurrently to this proxy server. For more details, please refer to the user guide
inside the docs folder.

Follows Intel Specification version 2.1 for PXE boot process.
Supports ipxe and gpxe clients.
Supports clients whose system architecture is a nonzero value.

Note that the client gets the files themselves from a tftp server. This project does not provide a tftp 
server, you as the user must find and configure one independently. This project, the proxy DHCP server,
serves to redirect the client to the tftp server with the correct filename. 
