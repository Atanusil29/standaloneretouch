function generateDocument() {
    var radio = document.getElementsByName('template');
    for (i = 0; i < radio.length; i++) {
        if (radio[i].checked)
            var radTemplate = radio[i].value;
    }
    if (radTemplate != null) {
        $("#dialog-confirm").dialog({
            resizable: false,
            height: "auto",
            width: 400,
            modal: true,
            buttons: {
                "Create layout": function() {
                    //$( this ).dialog( "close" );
                    // check if the channel is present
                    var channelName = $('#channel :selected').val();
                    //alert($('#channel :selected').val());
                    if ($('#channel :selected').val() != "") {

                        $(function() {
                            $("#dialog-confirm-email").dialog({
                                resizable: false,
                                height: "auto",
                                width: 400,
                                modal: true,
                                buttons: {
                                    "Submit": function() {

                                        var emailAddress = $('#email').val();
                                        var emailSubject = $('#emailSubect').val();
                                        if (emailAddress != "" & emailSubject != "") {
                                            var modal = $('<div>').dialog({
                                                modal: true
                                            });
                                            modal.dialog('widget').hide();
                                            $('#spinner').show();
                                            var datas = {
                                                "radTemplate": radTemplate,
                                                "channel": channelName,
                                                "emailAddress": emailAddress,
                                                "emailSubject": emailSubject
                                            }
                                            $.ajax({
                                                type: "POST",
                                                contentType: 'application/json; charset=utf-8',
                                                url: "/ReTouchFrameworkapp/fetchdocumenturl",
                                                data: JSON.stringify(datas)
                                            }).done(function(data) {
                                                $('#spinner').hide();
                                                //modal.dialog('close');
                                                // Split the datastream and store into vriables. This is important for using SubMit calls
                                                let splittedData = data.split('|');
                                                //alert(splittedData[0]); ReTouch URL
                                                //alert(splittedData[1]); OTDSTicket
                                                //alert(splittedData[2]); DocId
                                                //alert(splittedData[3]); Revision
                                                //alert(splittedData[4]); MessageId
                                                //alert(splittedData[5]); jSessionId
                                                var $dialog = $('<div></div>')
                                                    .html('<iframe style="border: 0px; " src="' + splittedData[0] + '" width="1200" height="800"></iframe>')
                                                    .dialog({
                                                        autoOpen: false,
                                                        modal: true,
                                                        height: 625,
                                                        width: 500,
                                                        title: "Compose email here",
                                                        buttons: {
                                                            "Submit": function() {
                                                                $(document).ready(function() {
                                                                    $("#dialog-confirm-submit").dialog({
                                                                        resizable: false,
                                                                        height: "auto",
                                                                        width: 400,
                                                                        modal: true,
                                                                        buttons: {
                                                                            "Submit document": function() {
                                                                                var modal = $('<div>').dialog({
                                                                                    modal: true
                                                                                });
                                                                                modal.dialog('widget').hide();
                                                                                $("#dialog-confirm-submit").dialog('close');
                                                                                $('#spinner').show();
                                                                                var datas = {
                                                                                    "otdsticket": splittedData[1],
                                                                                    "docid": splittedData[2],
                                                                                    "revision": splittedData[3],
                                                                                    "messageid": splittedData[4],
                                                                                    "jsessionId": splittedData[5]
                                                                                }
                                                                                $.ajax({
                                                                                    type: "POST",
                                                                                    contentType: 'application/json; charset=utf-8',
                                                                                    dataType: 'json',
                                                                                    url: "/ReTouchFrameworkapp/SubmitDocument",
                                                                                    data: JSON.stringify(datas),
                                                                                    success: function(data) {
                                                                                        $('#spinner').hide();
                                                                                        modal.dialog('close');
                                                                                        $("#dialog-confirm-submit").dialog('close');
                                                                                        $(function() {
                                                                                            $("#dialog-confirm-submit-done").dialog({
                                                                                                resizable: false,
                                                                                                height: "auto",
                                                                                                width: 400,
                                                                                                modal: true,
                                                                                                buttons: {
                                                                                                    "Create a new document": function() {
                                                                                                        $(".button").prop('disabled', true).css('opacity', 0.5);
                                                                                                        location.reload(true);
                                                                                                    }
                                                                                                }
                                                                                            });
                                                                                        });
                                                                                    },
                                                                                    error: function(e) {}
                                                                                });
                                                                            },
                                                                            Cancel: function() {
                                                                                $(this).dialog("close");
                                                                            }
                                                                        }
                                                                    });
                                                                });


                                                            },
                                                            "Cancel": function() {
                                                                $(this).dialog("close");
                                                                $("#dialog-confirm").dialog("close");
                                                                $(document).ready(function() {
                                                                    location.reload(true);
                                                                });
                                                            }
                                                        },
                                                    });
                                                $dialog.dialog('open');

                                            });
                                        } else {
                                            $("#dialog-confirm-email-selection").dialog({
                                                resizable: false,
                                                height: "auto",
                                                width: 400,
                                                modal: true,
                                                buttons: {
                                                    Cancel: function() {
                                                        $(this).dialog("close");
                                                    }
                                                }
                                            });
                                        }
                                    },
                                    Cancel: function() {
                                        $(this).dialog("close");
                                    }
                                }
                            });
                        });
                    } else {
                        $("#dialog-confirm-channel-selection").dialog({
                            resizable: false,
                            height: "auto",
                            width: 400,
                            modal: true,
                            buttons: {
                                Cancel: function() {
                                    $(this).dialog("close");
                                }
                            }
                        });
                    }
                },
                Cancel: function() {
                    $(this).dialog("close");
                }

            }

        });

    } else {
        $(function() {
            $("#dialog-confirm-sselection").dialog({
                resizable: false,
                height: "auto",
                width: 400,
                modal: true,
                buttons: {
                    Cancel: function() {
                        $(this).dialog("close");
                    }
                }
            });
        });
    }
}