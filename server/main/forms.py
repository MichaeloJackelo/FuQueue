from wtforms import DecimalField, IntegerField
from wtforms import validators
from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, BooleanField, SubmitField, \
    TextAreaField
from wtforms.validators import ValidationError, DataRequired, Email, EqualTo, \
    Length
from main.models import User


class ProductForm(FlaskForm):
    name = StringField('name', validators=[validators.required()])
    prize =  DecimalField('prize', validators=[validators.required()])
    file_name = StringField('Image file name', validators=[validators.required()])
    barcode = IntegerField('barcode', validators=[validators.required()])
    description = TextAreaField('description', validators=[validators.DataRequired()])
    submit = SubmitField('Submit')


class CategoryForm(FlaskForm):
    name = StringField('name', validators=[validators.required()])
    description = TextAreaField('description', validators=[validators.DataRequired()])
    submit = SubmitField('Submit')


class CountryOfOriginForm(FlaskForm):
    name = StringField('Country name', validators=[validators.required()])
    submit = SubmitField('Submit')


class MapForm(FlaskForm):
    product_id = IntegerField('Product id', validators=[validators.required()])
    object_id = IntegerField('Object id', validators=[validators.required()])
    submit = SubmitField('Submit')

class DeleteForm(FlaskForm):
    id = IntegerField('Id', validators=[validators.required()])
    submit = SubmitField('Submit')


class LoginForm(FlaskForm):
    username = StringField('Username', validators=[DataRequired()])
    password = PasswordField('Password', validators=[DataRequired()])
    remember_me = BooleanField('Remember Me')
    submit = SubmitField('Sign In')


class RegistrationForm(FlaskForm):
    username = StringField('Username', validators=[DataRequired()])
    email = StringField('Email', validators=[DataRequired(), Email()])
    password = PasswordField('Password', validators=[DataRequired()])
    password2 = PasswordField(
        'Repeat Password', validators=[DataRequired(), EqualTo('password')])

    def validate_username(self, username):
        user = User.query.filter_by(username=username.data).first()
        if user is not None:
            raise ValidationError('Please use a different username.')

    def validate_email(self, email):
        user = User.query.filter_by(email=email.data).first()
        if user is not None:
            raise ValidationError('Please use a different email address.')


class UserRegistrationForm(RegistrationForm):
    submit = SubmitField('Register')


class AdminRegistrationForm(RegistrationForm):
    admin_token = PasswordField('Token', validators=[DataRequired()])
    submit = SubmitField('Register')

    def validate_admin_token(self, admin_token):
        if admin_token.data != "qouvadis":
            raise ValidationError('Please use current token')


class ResetPasswordRequestForm(FlaskForm):
    email = StringField('Email', validators=[DataRequired(), Email()])
    submit = SubmitField('Request Password Reset')


class ResetPasswordForm(FlaskForm):
    password = PasswordField('Password', validators=[DataRequired()])
    password2 = PasswordField(
        'Repeat Password', validators=[DataRequired(), EqualTo('password')])
    submit = SubmitField('Request Password Reset')


class EditProfileForm(FlaskForm):
    username = StringField('Username', validators=[DataRequired()])
    about_me = TextAreaField('About me', validators=[Length(min=0, max=140)])
    submit = SubmitField('Submit')

    def __init__(self, original_username, *args, **kwargs):
        super(EditProfileForm, self).__init__(*args, **kwargs)
        self.original_username = original_username

    def validate_username(self, username):
        if username.data != self.original_username:
            user = User.query.filter_by(username=self.username.data).first()
            if user is not None:
                raise ValidationError('Please use a different username.')