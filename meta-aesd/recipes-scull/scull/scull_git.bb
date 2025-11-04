# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://git@github.com/cu-ecen-aeld/assignment-7-ryed5569.git;protocol=ssh;branch=master \
           file://0001-LDD3-build-only-scull-and-misc-modules-include-path-.patch \
           file://scull-start-stop \
           "

# Modify these as desired
PV = "1.0+git${SRCPV}"
SRCREV = "2e16081e6cfd66d92f09f208648ad732b81dee2b"

S = "${WORKDIR}/git"

inherit module update-rc.d
DEPENDS += "virtual/kernel"

# Build only the scull subdir using the kernel build tree
# Hint iv: use -C ${STAGING_KERNEL_DIR} and M=${S}/scull
EXTRA_OEMAKE = " -C ${STAGING_KERNEL_DIR} M=${S}/scull \
                 EXTRA_CFLAGS='-I${S}/include -I${S}/scull'"
#MODULES_INSTALL_TARGET = "all"

# The module class do_compile calls 'make modules'
# The module class do_install calls 'make INSTALL_MOD_PATH=${D} modules_install'
# Point INSTALL_MOD_DIR to 'extra' so they land in .../extra
#MODULES_INSTALL_TARGET = "modules_install"
INSTALL_MOD_DIR = "extra"

# Package contents
FILES:${PN} += "${libdir}/modules/${KERNEL_VERSION}/extra/scull.ko"
FILES:${PN} += "${sysconfdir}/init.d/scull-start-stop"

# Init script via update-rc.d
INITSCRIPT_PACKAGES = "${PN}"
INITSCRIPT_NAME:${PN} = "scull-start-stop"
INITSCRIPT_PARAMS:${PN} = "defaults 99"

RPROVIDES:${PN} += "kernel-module-scull"

# Install init script
do_install:append() {
    install -d ${D}${libdir}/modules/${KERNEL_VERSION}/extra/
	install -m 0755 ${S}/scull/scull.ko ${D}${libdir}/modules/${KERNEL_VERSION}/extra/
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${WORKDIR}/scull-start-stop ${D}${sysconfdir}/init.d/
}