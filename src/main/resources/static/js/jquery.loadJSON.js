/*
 * File:        jquery.loadJSON.js
 * Version:     1.0.0.
 * Author:      Jovan Popovic 
 * 
 * Copyright 2011 Jovan Popovic, all rights reserved.
 *
 * This source file is free software, under either the GPL v2 license or a
 * BSD style license, as supplied with this software.
 * 
 * This source file is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. 
 *
 * This file contains implementation of the JQuery templating engine that load JSON
 * objects into the HTML code. It is based on Alexandre Caprais notemplate plugin 
 * with several enchancements that are added to this plugin.
 */
(function($) {
    $.fn.toJSON = function() {
        var $elements = {};
        var $form = $(this);
        $form.find('input, select, textarea').each(function() {
            var name = $(this).attr('name')
            var type = $(this).attr('type')
            if (name) {
                var $value;
                if (type == 'radio') {
                    $value = $('input[name=' + name + ']:checked', $form).val()
                } else if (type == 'checkbox') {
                    $value = $(this).is(':checked')
                } else {
                    $value = $(this).val()
                }
                $elements[$(this).attr('name')] = $value
            }
        });
        return JSON.stringify($elements)
    };
    $.fn.formToJSON = function() {
        var $elements = {};
        var $form = $(this);
        $form.find('input, select, textarea').each(function() {
            var name = $(this).attr('name')
            var type = $(this).attr('type')
            if (name) {
                var $value;
                if (type == 'radio') {
                    $value = $('input[name=' + name + ']:checked', $form).val()
                } else if (type == 'checkbox') {
                    $value = $(this).is(':checked')
                } else {
                    $value = $(this).val()
                }
                $elements[$(this).attr('name')] = $value
            }
        });
        return $elements;
    };
    $.fn.fromJSON = function(data) {
        var $form = $(this);
        $.each(data, function(key, value) {
            $('dd[aria-label=' + key + ']', $form).html(value);
            var $elem = $('[name="' + key + '"]', $form)
            var type = $elem.first().attr('type')
            if (type == 'radio') {
                $('[name="' + key + '"][value="' + value + '"]').prop('checked', true)
            } else if (type == 'checkbox' && (value == true || value == 'true')) {
                $('[name="' + key + '"]').prop('checked', true)
            } else {
                $elem.val(value)
            }
        });
    };
    $.fn.loadJSON = function(data) {
        var $form = $(this);
        $.each(data, function(key, value) {
            $('dd[aria-label=' + key + ']', $form).html(value);
            $('td[aria-label=' + key + ']', $form).html(value);
            $('b[aria-label=' + key + ']', $form).html(value);
            $('span[aria-label=' + key + ']', $form).html(value);
            var $elem = $('[name="' + key + '"]', $form)
            var type = $elem.first().attr('type')
            if (type == 'radio') {
                $('[name="' + key + '"][value="' + value + '"]').prop('checked', true)
            } else if (type == 'checkbox' && (value == true || value == 'true')) {
                $('[name="' + key + '"]').prop('checked', true)
            } else {
                $elem.val(value)
            }
        });
    };
}(jQuery));