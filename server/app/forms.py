from flask_wtf import FlaskForm
from wtforms import StringField, SubmitField, DecimalField, IntegerField, FileField, TextAreaField
from wtforms import validators


class ProductForm(FlaskForm):
    name = StringField('name', validators=[validators.required()])
    prize =  DecimalField('prize', validators=[validators.required()])
    file_name = StringField('Image file name', validators=[validators.required()])
    barcode = IntegerField('barcode', validators=[validators.required()])
    description = TextAreaField('description', validators=[validators.DataRequired()])
    submit = SubmitField('Submit')