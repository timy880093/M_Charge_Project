


<ul id="menu"></ul>
<script type="text/javascript">


    $(function () {

        $.getJSON("<%=request.getContextPath()%>/backendAdmin/menuServlet",
                function (data) {
                    if (data != null) {
                        var $menu = $("#menu");
                        $.each(data, function () {
                            $menu.append(
                                    getMenuItem(this)
                            );
                        });
                    }
                });
    });


    var getMenuItem = function (itemData) {
        var item;
        if(itemData.pop=='true'){
            item = $("<li class='dropdown'>")
                    .append('<a href="'+itemData.url+'" data-toggle="dropdown">' + itemData.name + '</a>');
        } else{
            item = $("<li class='dropdown'><input type='checkbox' />")
                    .append('<a href="javascript:void(0)" data-toggle="dropdown">' + itemData.name + '<i class="icon-arrow"></i></a>');
        }

        if (itemData.children) {
            var subList = $("<ul class='dropdown-menu'>");
            $.each(itemData.children, function () {
                subList.append(getSubMenuItem(this));
            });
            item.append(subList);
        }
        return item;
    };

    var getSubMenuItem = function (itemData) {       var item = '';

            item = $("<li>")
                    .append(
                            $("<a>", {
                                href: itemData.url,

                                html: itemData.name
                            }));


        return item;
    };


</script>

