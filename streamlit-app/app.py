import streamlit as st

st.set_page_config(
    page_title="LogEasy.io",
    page_icon="streamlit-app/images/logo.png",
    initial_sidebar_state="expanded"
)
st.sidebar.info("Work Under Progress")
st.sidebar.title("LogEasy.io")


def load_content(header: str, content_path: str):
    st.header(header)
    with open("streamlit-app/content/" + content_path) as f:
        st.markdown(f.read())


sections = ["Introduction", "Architecture and Components"]
content = ["Introduction.markdown", "Architecture_Components.markdown"]

section = st.sidebar.radio("Select one :", sections, index=0)
load_content(section, content[sections.index(section)])
