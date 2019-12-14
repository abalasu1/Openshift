resource "null_resource" "setup_storage" {
count = "${var.storage_count}"
  connection {
    type     = "ssh"
    user     = "root"
    host = "${element(var.storage_ip_address, count.index)}"
    private_key = "${file(var.storage_private_ssh_key)}"
  }
  provisioner "file" {
    source      = "${path.cwd}/scripts"
    destination = "/tmp"
   
  }

  provisioner "file" {
    source      = "${path.cwd}/localrepo/"
    destination = "/tmp"
  }
  provisioner "remote-exec" {
    inline = [
      "sed -i 's/<localrepo_ip>/${var.localrepo_ip}/g' /tmp/ose.repo",
      "cat /tmp/ose.repo > /etc/yum.repos.d/ose.repo"
    ]
  }


    provisioner "remote-exec" {
    inline = [
      "chmod +x /tmp/scripts/*",
      "/tmp/scripts/rhn_localrepo.sh ${var.rhn_username} ${var.rhn_password} ${var.localrepo_ip}",
      ]
  
    }

}
